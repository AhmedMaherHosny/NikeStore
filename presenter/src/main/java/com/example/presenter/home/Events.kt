package com.example.presenter.home

import com.example.core.base.BaseEvent
import com.example.core.enums.Sex
import com.example.domain.models.OrderItemDomainModel
import com.example.presenter.models.OrderItemUiModel

sealed class HomeEvent : BaseEvent {
    data class OnSexFilterClicked(val sex: Sex) : HomeEvent()
    data class OnSavedIconClicked(val isSaved: Boolean, val productId: String) : HomeEvent()
    data class OnShoppingIconClicked(
        val isInShoppingBag: Boolean,
        val orderItemUiModel: OrderItemUiModel
    ) : HomeEvent()
}


