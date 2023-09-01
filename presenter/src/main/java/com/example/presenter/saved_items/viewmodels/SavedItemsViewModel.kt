package com.example.presenter.saved_items.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.base.StateErrorType
import com.example.domain.usecases.get_saved_items.GetSavedItemsUseCase
import com.example.domain.usecases.unsave_product.UnSaveProductUseCase
import com.example.presenter.mappers.toProductItemUiModel
import com.example.presenter.saved_items.SavedItemsEvent
import com.example.presenter.saved_items.SavedItemsNavigator
import com.example.presenter.saved_items.states.SavedItemsViewState
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
class SavedItemsViewModel @Inject constructor(
    private val getSavedItemsUseCase: GetSavedItemsUseCase,
    private val unSaveProductUseCase: UnSaveProductUseCase
) : ViewModel() {
    var savedItemsViewState by mutableStateOf(SavedItemsViewState())
        private set

    private val _eventClicks = MutableSharedFlow<SavedItemsEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()

    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()

    private val initNavigator: SavedItemsNavigator? by lazy { null }

    private val _navigator = MutableStateFlow(initNavigator)
    val navigator get() = _navigator.asStateFlow().filterNotNull()

    private val firebaseAuthExceptionHandlers = CoroutineExceptionHandler { _, exception ->
        savedItemsViewState = savedItemsViewState.copy(isLoading = false)
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


    init {
        subscribeEvents()
        loadSavedItems()
    }

    private fun loadSavedItems() {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            savedItemsViewState = savedItemsViewState.copy(isLoading = true)
            val products = getSavedItemsUseCase(appUserUiModel?.id!!).map {
                it.toProductItemUiModel()
            }
            savedItemsViewState =
                savedItemsViewState.copy(isLoading = false, productsItemUiModel = products)
        }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {
                handleEvents(it)
            }
        }
    }

    private fun handleEvents(event: SavedItemsEvent) {
        when (event) {
            is SavedItemsEvent.OnSavedIconClicked -> {
                removeItem(event.productItemUiModel.id!!)
                val updatedProducts = savedItemsViewState.productsItemUiModel.toMutableList()
                updatedProducts.removeIf { it == event.productItemUiModel }
                savedItemsViewState =
                    savedItemsViewState.copy(productsItemUiModel = updatedProducts)
            }
        }
    }

    private fun removeItem(id: String) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            unSaveProductUseCase(appUserUiModel?.id!!, id)
        }
    }

    fun setEventClicks(event: SavedItemsEvent) {
        viewModelScope.launch {
            _eventClicks.emit(event)
        }
    }

    private fun setEventError(event: StateErrorType) {
        viewModelScope.launch {
            _eventError.emit(event)
        }
    }

    fun setNavigator(builder: () -> SavedItemsNavigator?) {
        viewModelScope.launch {
            _navigator.emit(builder())
        }
    }
}