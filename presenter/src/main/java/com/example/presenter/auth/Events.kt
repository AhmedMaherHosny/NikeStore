package com.example.presenter.auth

import com.example.core.base.BaseEvent

sealed class AuthEvent : BaseEvent {
    object OnLoginClicked : AuthEvent()
    object OnRegisterClicked : AuthEvent()
}
