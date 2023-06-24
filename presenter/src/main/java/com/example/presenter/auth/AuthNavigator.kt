package com.example.presenter.auth

import com.example.core.BaseState

sealed class AuthNavigator : BaseState {
    object NavigatToPhoneVerificationScreen : AuthNavigator()
}