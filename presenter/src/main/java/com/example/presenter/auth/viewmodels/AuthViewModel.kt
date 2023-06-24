package com.example.presenter.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presenter.auth.AuthEvent
import com.example.presenter.auth.states.LoginViewState
import com.example.presenter.auth.states.RegisterViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

) : ViewModel() {
    var loginViewState by mutableStateOf(LoginViewState())
        private set
    var registerViewState by mutableStateOf(RegisterViewState())
        private set

    private val _event = MutableSharedFlow<AuthEvent>()
    private val event get() = _event.asSharedFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
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

            }

            AuthEvent.OnFacebookClicked -> {

            }
        }
    }

    private fun login() {
        loginViewState = loginViewState.copy(isLoading = true)
        loginViewState = loginViewState.copy(isLoading = false)
    }

    private fun register() {
        registerViewState = registerViewState.copy(isLoading = true)
        registerViewState = registerViewState.copy(isLoading = false)
    }

    fun setEvent(event: AuthEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}