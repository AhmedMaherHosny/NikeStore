package com.example.presenter.saved_items

import com.example.core.base.BaseEvent
import com.example.presenter.models.ProductItemUiModel

sealed class SavedItemsEvent : BaseEvent {
    data class OnSavedIconClicked(val productItemUiModel: ProductItemUiModel) : SavedItemsEvent()
}