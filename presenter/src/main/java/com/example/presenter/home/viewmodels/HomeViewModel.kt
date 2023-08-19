package com.example.presenter.home.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.base.StateErrorType
import com.example.core.enums.Sex
import com.example.domain.usecases.get_products_grouped_by_category_and_sex.GetProductsGroupedByCategoryAndSexUseCase
import com.example.presenter.home.HomeEvent
import com.example.presenter.home.HomeNavigator
import com.example.presenter.home.states.HomeViewState
import com.example.presenter.mappers.toProductItemUiModel
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
class HomeViewModel @Inject constructor(
    private val getProductsGroupedByCategoryAndSexUseCase: GetProductsGroupedByCategoryAndSexUseCase
) : ViewModel() {
    var homeViewState by mutableStateOf(HomeViewState())
    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()
    private val _eventClicks = MutableSharedFlow<HomeEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()
    private val initNavigator: HomeNavigator? by lazy { null }
    private val _navigator = MutableStateFlow(initNavigator)
    val navigator get() = _navigator.asStateFlow().filterNotNull()
    private val firebaseAuthExceptionHandlers = CoroutineExceptionHandler { _, exception ->
        homeViewState = homeViewState.copy(isLoading = false)
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
        handleOnSexFilterClicked()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {
                handleEvents(it)
            }
        }
    }

    private fun handleEvents(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSexFilterClicked -> {
                handleOnSexFilterClicked(event.sex)
            }
        }
    }

    private fun handleOnSexFilterClicked(sex: Sex = Sex.MEN) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            homeViewState = homeViewState.copy(isLoading = true)
            val productsDomainModel = getProductsGroupedByCategoryAndSexUseCase(sex)
            val productsUiModel = productsDomainModel.mapValues { (_, productsDomainModel) ->
                productsDomainModel.map { productsDomainModel -> productsDomainModel.toProductItemUiModel() }
            }
            homeViewState =
                homeViewState.copy(productsGroupedByCategory = productsUiModel, isLoading = false)
        }
    }

    fun setNavigator(builder: () -> HomeNavigator?) {
        viewModelScope.launch {
            _navigator.emit(builder())
        }
    }

    fun setEventClicks(event: HomeEvent) {
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


