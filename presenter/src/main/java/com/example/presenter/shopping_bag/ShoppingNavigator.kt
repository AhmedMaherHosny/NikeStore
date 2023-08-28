package com.example.presenter.shopping_bag

import com.example.core.base.BaseState

sealed class ShoppingNavigator : BaseState {
    object NavigateToOrderPlacedScreen : ShoppingNavigator()

}