package com.example.presenter.profile.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Constants.USER_MODEL
import com.example.core.base.StateErrorType
import com.example.domain.models.AppUserDomainModel
import com.example.domain.usecases.read_user_datastore.ReadUserDataFromDatastoreUseCase
import com.example.domain.usecases.update_user_in_firestore.UpdateUserInFireStoreUseCase
import com.example.domain.usecases.write_user_datastore.WriteUserDataToDatastoreUseCase
import com.example.presenter.mappers.toAppUserUiModel
import com.example.presenter.models.AppUserUiModel
import com.example.presenter.profile.ProfileEvent
import com.example.presenter.profile.states.ProfileViewState
import com.example.presenter.utils.appUserUiModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val readUserDataFromDatastoreUseCase: ReadUserDataFromDatastoreUseCase,
    private val writeUserDataFromDatastoreUseCase: WriteUserDataToDatastoreUseCase,
    private val updateUserInFireStoreUseCase: UpdateUserInFireStoreUseCase
    ) : ViewModel() {
    var profileViewState by mutableStateOf(ProfileViewState())
        private set
    private val _eventClicks = MutableSharedFlow<ProfileEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()

    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()
    private val firebaseAuthExceptionHandlers = CoroutineExceptionHandler { _, exception ->
        profileViewState = profileViewState.copy(isLoading = false)
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
        loadUserData()
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {
                handleEvents(it)
            }
        }
    }

    private fun handleEvents(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnSaveClicked -> {
                val userDomainModel = AppUserDomainModel(
                    id = appUserUiModel?.id,
                    name = profileViewState.username.text.value,
                    password = profileViewState.password.text.value,
                    email = profileViewState.email.text.value,
                    phone = profileViewState.phoneNumber.text.value
                )
                updateUserFireBase(userDomainModel)
            }
        }
    }

    private fun updateUserFireBase(userDomainModel: AppUserDomainModel) {
        viewModelScope.launch(firebaseAuthExceptionHandlers){
            updateUserInFireStoreUseCase(userDomainModel)
            writeUserDataFromDatastoreUseCase(USER_MODEL, userDomainModel)
            updateAppUserUi(userDomainModel.toAppUserUiModel())
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userDomainModel = readUserDataFromDatastoreUseCase(USER_MODEL)
            val userUiModel = userDomainModel?.toAppUserUiModel()
            profileViewState = profileViewState.copy(
                username = profileViewState.username.apply {
                    text.value = userUiModel?.name ?: ""
                },
                email = profileViewState.email.apply {
                    text.value = userUiModel?.email ?: ""
                },
                phoneNumber = profileViewState.phoneNumber.apply {
                    text.value = userUiModel?.phone ?: ""
                },
            )
        }
    }

    fun setEventClicks(event: ProfileEvent) {
        viewModelScope.launch {
            _eventClicks.emit(event)
        }
    }

    private fun setEventError(event: StateErrorType) {
        viewModelScope.launch {
            _eventError.emit(event)
        }
    }

    private fun updateAppUserUi(appUserUiModelRS: AppUserUiModel) {
        appUserUiModel = appUserUiModelRS
    }
}