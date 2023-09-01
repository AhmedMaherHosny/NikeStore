package com.example.presenter.profile

import com.example.core.base.BaseEvent

sealed class ProfileEvent : BaseEvent {
    object OnSaveClicked : ProfileEvent()
}
