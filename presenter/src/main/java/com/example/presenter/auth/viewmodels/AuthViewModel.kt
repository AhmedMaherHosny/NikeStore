package com.example.presenter.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Constants.USER_MODEL
import com.example.core.base.StateErrorType
import com.example.domain.usecases.add_user_to_firestore.AddUserToFireStoreUseCase
import com.example.domain.usecases.create_user_by_email_password.CreateUserByEmailAndPasswordUseCase
import com.example.domain.usecases.get_user_data_from_server.GetUserDataFromServerUseCase
import com.example.domain.usecases.login_user_by_email_and_password.LoginUserByEmailAndPasswordUseCase
import com.example.domain.usecases.write_user_datastore.WriteUserDataToDatastoreUseCase
import com.example.presenter.auth.AuthEvent
import com.example.presenter.auth.AuthNavigator
import com.example.presenter.auth.google_auth.GoogleSignInResult
import com.example.presenter.auth.states.LoginViewState
import com.example.presenter.auth.states.RegisterViewState
import com.example.presenter.mappers.toAppUserDomainModel
import com.example.presenter.mappers.toAppUserUiModel
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
class AuthViewModel @Inject constructor(
    private val createUserByEmailAndPasswordUseCase: CreateUserByEmailAndPasswordUseCase,
    private val addUserToFireStoreUseCase: AddUserToFireStoreUseCase,
    private val writeUserDataToDatastoreUseCase: WriteUserDataToDatastoreUseCase,
    private val loginUserByEmailAndPasswordUseCase: LoginUserByEmailAndPasswordUseCase,
    private val getUserDataFromServerUseCase: GetUserDataFromServerUseCase,
) : ViewModel() {
    var loginViewState by mutableStateOf(LoginViewState())
        private set
    var registerViewState by mutableStateOf(RegisterViewState())
        private set

    private val _eventClicks = MutableSharedFlow<AuthEvent>()
    private val eventClicks get() = _eventClicks.asSharedFlow()

    private val _eventError = MutableSharedFlow<StateErrorType>()
    val eventError get() = _eventError.asSharedFlow()

    private val initNavigator: AuthNavigator? by lazy { null }

    private val _navigator = MutableStateFlow(initNavigator)
    val navigator get() = _navigator.asStateFlow().filterNotNull()


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
        }
    }

    fun google(googleSignInResult: GoogleSignInResult) {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            loginViewState = loginViewState.copy(isLoading = true)
            if (googleSignInResult.user != null) {
                var appUserDomainModel = googleSignInResult.user.toAppUserDomainModel()
                appUserDomainModel = addUserToFireStoreUseCase(appUserDomainModel)
                writeUserDataToDatastoreUseCase(USER_MODEL, appUserDomainModel)
                appUserUiModel = appUserDomainModel.toAppUserUiModel()
                loginViewState = loginViewState.copy(isLoading = false)
                setNavigator { AuthNavigator.NavigateToHomeScreen }
                return@launch
            }
            Timber.e(googleSignInResult.error)
        }
    }

    private fun login() {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            loginViewState = loginViewState.copy(isLoading = true)
            val userUid = loginUserByEmailAndPasswordUseCase(
                loginViewState.email.text.value,
                loginViewState.password.text.value
            )
            val appUserDomainModel = getUserDataFromServerUseCase(userUid)
            writeUserDataToDatastoreUseCase(USER_MODEL, appUserDomainModel)
            appUserUiModel = appUserDomainModel.toAppUserUiModel()
            loginViewState = loginViewState.copy(isLoading = false)
            setNavigator { AuthNavigator.NavigateToHomeScreen }
        }
    }

    private fun register() {
        viewModelScope.launch(firebaseAuthExceptionHandlers) {
            registerViewState = registerViewState.copy(isLoading = true)
            val authUser = createUserByEmailAndPasswordUseCase(
                registerViewState.email.text.value,
                registerViewState.password.text.value
            ).copy(
                name = registerViewState.username.text.value,
                password = registerViewState.password.text.value,
                phone = registerViewState.phoneNumber.text.value,
            )
            val appUserDomainModel = addUserToFireStoreUseCase(authUser)
            writeUserDataToDatastoreUseCase(USER_MODEL, appUserDomainModel)
            appUserUiModel = appUserDomainModel.toAppUserUiModel()
            registerViewState = registerViewState.copy(isLoading = false)
            setNavigator { AuthNavigator.NavigateToHomeScreen }
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

    fun setNavigator(builder: () -> AuthNavigator?) {
        viewModelScope.launch {
            _navigator.emit(builder())
        }
    }

}