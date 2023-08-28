package com.example.presenter.shopping_bag

import com.example.core.base.BaseEvent
import com.example.core.enums.Sex
import com.example.presenter.models.OrderUiModel
import com.example.presenter.product.ProductEvent

sealed class ShoppingEvent : BaseEvent {
    data class OnOrderButtonClicked(val orderUiModel: OrderUiModel) : ShoppingEvent()
}


