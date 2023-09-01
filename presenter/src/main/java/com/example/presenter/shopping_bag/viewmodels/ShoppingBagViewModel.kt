package com.example.presenter.shopping_bag.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.base.StateErrorType
import com.example.domain.models.OrderDomainModel
import com.example.domain.usecases.get_address.GetAddressUseCase
import com.example.domain.usecases.get_products_from_shopping_bag.GetProductsFromShoppingBagUseCase
import com.example.domain.usecases.place_an_order.PlaceAnOrderUseCase
import com.example.presenter.mappers.toAddressUiModel
import com.example.presenter.mappers.toOrderDomainModel
import com.example.presenter.mappers.toOrderItemUiModel
import com.example.presenter.models.AddressUiModel
import com.example.presenter.shopping_bag.ShoppingEvent
import com.example.presenter.shopping_bag.ShoppingNavigator
import com.example.presenter.shopping_bag.states.ShoppingViewState
import com.example.presenter.utils.appUserUiModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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
class ShoppingBagViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductsFromShoppingBagUseCase: GetProductsFromShoppingBagUseCase,
    private val placeAnOrderUseCase: PlaceAnOrderUseCase,
    private val getAddressUseCase: GetAddressUseCase
) : ViewModel() {
    var shoppingBagViewState by mutableStateOf(ShoppingViewState())
        private set
    private val _eventClicks = MutableSharedFlow<ShoppingEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()

    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()

    private val initNavigator: ShoppingNavigator? by lazy { null }

    private val _navigator = MutableStateFlow(initNavigator)
    val navigator get() = _navigator.asStateFlow().filterNotNull()

    private val firebaseAuthExceptionHandlers = CoroutineExceptionHandler { _, exception ->
        shoppingBagViewState = shoppingBagViewState.copy(isLoading = false)
        Timber.e("the reason " + exception.cause)
        when (exception) {
            is FirebaseNetworkException, is UnknownHostException, is IOException -> {
                setEventError(StateErrorType.NETWORK_ERROR)
            }

            is FirebaseAuthUserCollisionException -> {
                Timber.e("The email address is already in use by another account")
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
    private var orderId: String? = null

    init {
        orderId = savedStateHandle.get<String>("orderId")
        subscribeEvents()
        getAddress()
        if (orderId == null) {
            loadProductsFromShoppingBag()
        } else {

        }
    }

    private fun getAddress() {
        viewModelScope.launch {
            val address = getAddressUseCase()
            shoppingBagViewState = shoppingBagViewState.copy(address = address.toAddressUiModel())
        }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {
                handleEvents(it)
            }
        }
    }

    private fun handleEvents(event: ShoppingEvent) {
        when (event) {
            is ShoppingEvent.OnOrderButtonClicked -> {
                val order = event.orderUiModel.toOrderDomainModel()
                order.userId = appUserUiModel?.id!!
                placeAnOrder(order)
            }
        }
    }


    private fun placeAnOrder(orderDomainModel: OrderDomainModel) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            shoppingBagViewState = shoppingBagViewState.copy(isLoading = true)
            placeAnOrderUseCase(orderDomainModel)
            shoppingBagViewState = shoppingBagViewState.copy(isLoading = false)
            setNavigator { ShoppingNavigator.NavigateToOrderPlacedScreen }
        }
    }

    private fun loadProductsFromShoppingBag() {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            shoppingBagViewState = shoppingBagViewState.copy(isLoading = true)
            val items = getProductsFromShoppingBagUseCase(appUserUiModel?.id!!)
            shoppingBagViewState = shoppingBagViewState.copy(
                isLoading = false,
                orderItemsUiModel = items.map { it.toOrderItemUiModel() })
        }
    }

    private fun setEventError(event: StateErrorType) {
        viewModelScope.launch {
            _eventError.emit(event)
        }
    }

    fun setNavigator(builder: () -> ShoppingNavigator?) {
        viewModelScope.launch {
            _navigator.emit(builder())
        }
    }

    fun setEventClicks(event: ShoppingEvent) {
        viewModelScope.launch {
            _eventClicks.emit(event)
        }
    }

    fun setNewAddress(addressUiModel: AddressUiModel) {
        shoppingBagViewState = shoppingBagViewState.copy(address = addressUiModel)
    }

}