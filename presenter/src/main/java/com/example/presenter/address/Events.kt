package com.example.presenter.address

import com.example.core.base.BaseEvent

sealed class AddressEvent : BaseEvent {
    object OnSaveButtonClicked : AddressEvent()
}


