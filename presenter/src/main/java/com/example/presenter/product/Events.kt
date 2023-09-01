package com.example.presenter.product

import com.example.core.base.BaseEvent
import com.example.presenter.models.OrderItemUiModel

sealed class ProductEvent : BaseEvent {
    data class OnSavedIconClicked(val isSaved: Boolean) : ProductEvent()
    data class OnShoppingButtonClicked(
        val isInShoppingBag: Boolean,
        val orderItemUiModel: OrderItemUiModel
    ) : ProductEvent()
}