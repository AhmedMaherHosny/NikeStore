package com.example.presenter.product.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.base.StateErrorType
import com.example.domain.models.OrderItemDomainModel
import com.example.domain.usecases.add_product_to_shopping_bag.AddProductToShoppingBagUseCase
import com.example.domain.usecases.get_product_by_id.GetProductByIdUseCase
import com.example.domain.usecases.remove_product_from_shopping_bag.RemoveProductFromShoppingBagUseCase
import com.example.domain.usecases.save_product.SaveProductUseCase
import com.example.domain.usecases.unsave_product.UnSaveProductUseCase
import com.example.presenter.mappers.toOrderItemDomainModel
import com.example.presenter.mappers.toProductItemUiModel
import com.example.presenter.product.ProductEvent
import com.example.presenter.product.ProductNavigator
import com.example.presenter.product.states.ProductViewState
import com.example.presenter.utils.appUserUiModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val saveProductUseCase: SaveProductUseCase,
    private val unSaveProductUseCase: UnSaveProductUseCase,
    private val addProductToShoppingBagUseCase: AddProductToShoppingBagUseCase,
    private val removeProductToShoppingBagUseCase: RemoveProductFromShoppingBagUseCase,
) : ViewModel() {
    var productViewState by mutableStateOf(ProductViewState())
    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()
    private val _eventClicks = MutableSharedFlow<ProductEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()
    private val initNavigator: ProductNavigator? by lazy { null }
    private val _navigator = MutableStateFlow(initNavigator)
    val navigator get() = _navigator.asStateFlow().filterNotNull()
    private val firebaseAuthExceptionHandlers = CoroutineExceptionHandler { _, exception ->
        productViewState = productViewState.copy(isLoading = false)
        Timber.e("the reason " + exception.cause)
        when (exception) {
            is FirebaseNetworkException, is UnknownHostException, is IOException -> {
                setEventError(StateErrorType.NETWORK_ERROR)
            }

            is FirebaseAuthInvalidCredentialsException -> {
                setEventError(StateErrorType.AUTHENTICATION_FAILED)
            }

            is FirebaseAuthInvalidUserException, is NullPointerException -> {
                Timber.e("Account is disabled, deleted, or does not exist")
                setEventError(StateErrorType.AUTHENTICATION_FAILED)
            }

            is FirebaseFirestoreException -> {
                Timber.e(exception.message)
            }

            else -> {
                Timber.e(exception, exception.message)
            }
        }
    }

    init {
        savedStateHandle.get<String>("id")?.let {
            subscribeEvents(it)
            loadProduct(it)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            productViewState = productViewState.copy(isLoading = true)
            val product = getProductByIdUseCase(appUserUiModel?.id!!, id).toProductItemUiModel()
            productViewState =
                productViewState.copy(isLoading = false, productItemUiModel = product)
        }
    }

    private fun subscribeEvents(id: String) {
        viewModelScope.launch {
            eventClicks.collect {
                when (it) {
                    is ProductEvent.OnSavedIconClicked -> {
                        if (!it.isSaved)
                            saveItem(id)
                        else
                            removeItem(id)
                    }
                    is ProductEvent.OnShoppingButtonClicked -> {
                        if (!it.isInShoppingBag)
                            addItemToShoppingList(it.orderItemUiModel.toOrderItemDomainModel())
                        else
                            removeItemToShoppingList(it.orderItemUiModel.toOrderItemDomainModel())
                    }
                }
            }
        }
    }

    private fun addItemToShoppingList(orderItemDomainModel: OrderItemDomainModel) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            println("before")
            addProductToShoppingBagUseCase(appUserUiModel?.id!!, orderItemDomainModel)
            println("after")
        }
    }

    private fun removeItemToShoppingList(orderItemDomainModel: OrderItemDomainModel) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            removeProductToShoppingBagUseCase(appUserUiModel?.id!!, orderItemDomainModel)
        }
    }

    private fun saveItem(id: String) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            saveProductUseCase(appUserUiModel?.id!!, id)
        }
    }

    private fun removeItem(id: String) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            unSaveProductUseCase(appUserUiModel?.id!!, id)
        }
    }

    fun setNavigator(builder: () -> ProductNavigator?) {
        viewModelScope.launch {
            _navigator.emit(builder())
        }
    }

    fun setEventClicks(event: ProductEvent) {
        viewModelScope.launch {
            _eventClicks.emit(event)
        }
    }

    private fun setEventError(event: StateErrorType) {
        viewModelScope.launch {
            _eventError.emit(event)
        }
    }
}