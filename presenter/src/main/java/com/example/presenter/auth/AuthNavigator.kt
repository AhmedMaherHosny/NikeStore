package com.example.presenter.auth

import com.example.core.base.BaseState

sealed class AuthNavigator : BaseState {
    object NavigateToHomeScreen : AuthNavigator()
}