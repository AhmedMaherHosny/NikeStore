package com.example.presenter.auth

import com.example.core.BaseEvent

sealed class AuthEvent : BaseEvent {
    object OnLoginClicked : AuthEvent()
    object OnRegisterClicked : AuthEvent()
    object OnGoogleClicked : AuthEvent()
    object OnFacebookClicked : AuthEvent()
}
