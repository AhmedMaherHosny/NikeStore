package com.example.presenter.home

import com.example.core.base.BaseState

sealed class HomeNavigator : BaseState {
    data class NavigateToProductScreen(val id : String) : HomeNavigator()
    object NavigateToAuthScreen : HomeNavigator()
}