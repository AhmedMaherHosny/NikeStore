package com.example.presenter.product.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.base.StateErrorType
import com.example.domain.usecases.get_product_by_id.GetProductByIdUseCase
import com.example.presenter.mappers.toProductItemUiModel
import com.example.presenter.product.ProductEvent
import com.example.presenter.product.ProductNavigator
import com.example.presenter.product.states.ProductViewState
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
    private val getProductByIdUseCase: GetProductByIdUseCase
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
        subscribeEvents()
        savedStateHandle.get<String>("id")?.let {
            loadProduct(it)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            productViewState = productViewState.copy(isLoading = true)
            val product = getProductByIdUseCase(id).toProductItemUiModel()
            productViewState =
                productViewState.copy(isLoading = false, productItemUiModel = product)
        }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {

            }
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