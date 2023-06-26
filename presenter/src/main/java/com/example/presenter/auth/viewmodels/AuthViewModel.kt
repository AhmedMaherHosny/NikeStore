package com.example.presenter.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Constants.USER_MODEL
import com.example.core.StateErrorType
import com.example.domain.usecases.add_user_to_firestore.AddUserToFireStoreUseCase
import com.example.domain.usecases.create_user_by_email_password.CreateUserByEmailAndPasswordUseCase
import com.example.domain.usecases.read_user_datastore.ReadUserDataFromDatastoreUseCase
import com.example.domain.usecases.write_user_datastore.WriteUserDataToDatastoreUseCase
import com.example.presenter.auth.AuthEvent
import com.example.presenter.auth.states.LoginViewState
import com.example.presenter.auth.states.RegisterViewState
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
class AuthViewModel @Inject constructor(
    private val createUserByEmailAndPasswordUseCase: CreateUserByEmailAndPasswordUseCase,
    private val addUserToFireStoreUseCase: AddUserToFireStoreUseCase,
    private val writeUserDataToDatastoreUseCase: WriteUserDataToDatastoreUseCase,
    private val readUserDataFromDatastoreUseCase: ReadUserDataFromDatastoreUseCase
) : ViewModel() {
    var loginViewState by mutableStateOf(LoginViewState())
        private set
    var registerViewState by mutableStateOf(RegisterViewState())
        private set

    private val _eventClicks = MutableSharedFlow<AuthEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()

    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()


    init {
        subscribeEvents()

    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            eventClicks.collect {
                handleEvents(it)
            }
        }
    }

    private fun handleEvents(event: AuthEvent) {
        when (event) {
            AuthEvent.OnLoginClicked -> {
                login()
            }

            AuthEvent.OnRegisterClicked -> {
                register()
            }

            AuthEvent.OnGoogleClicked -> {
                google()
            }

            AuthEvent.OnFacebookClicked -> {
                facebook()
            }
        }
    }

    private fun google() {

    }

    private fun facebook() {

    }

    private fun login() {
        loginViewState = loginViewState.copy(isLoading = true)
        loginViewState = loginViewState.copy(isLoading = false)
    }

    private fun register() {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            registerViewState = registerViewState.copy(isLoading = true)
            val authUser = createUserByEmailAndPasswordUseCase(
                registerViewState.email.text.value,
                registerViewState.password.text.value
            ).copy(
                username = registerViewState.username.text.value,
                phone = registerViewState.phoneNumber.text.value
            )
            val appUserDomainModel = addUserToFireStoreUseCase(authUser)
            writeUserDataToDatastoreUseCase(USER_MODEL, appUserDomainModel)
            Timber.e(appUserDomainModel.toString())
            Timber.e(readUserDataFromDatastoreUseCase(USER_MODEL).toString())
            registerViewState = registerViewState.copy(isLoading = false)
        }
    }

    fun setEventClicks(event: AuthEvent) {
        viewModelScope.launch {
            _eventClicks.emit(event)
        }
    }

    private fun setEventError(event: StateErrorType) {
        viewModelScope.launch {
            _eventError.emit(event)
        }
    }

    private val firebaseAuthExceptionHandlers = CoroutineExceptionHandler { _, exception ->
        registerViewState = registerViewState.copy(isLoading = false)
        loginViewState = loginViewState.copy(isLoading = false)
        when (exception) {
            is NullPointerException -> {
                Timber.e("Null pointer exception")
            }

            is FirebaseNetworkException, is UnknownHostException, is IOException -> {
                setEventError(StateErrorType.NETWORK_ERROR)
            }

            is FirebaseAuthUserCollisionException -> {
                Timber.e("The email address is already in use by another account")
            }

            is FirebaseAuthInvalidCredentialsException -> {
                setEventError(StateErrorType.AUTHENTICATION_FAILED)
            }

            is FirebaseAuthInvalidUserException -> {
                Timber.e("Account is disabled, deleted, or does not exist")
            }

            is FirebaseFirestoreException -> {
                Timber.e(exception.message)
            }

            else -> {
                Timber.e(exception, "Unknown error has occurred")
            }
        }
    }

}