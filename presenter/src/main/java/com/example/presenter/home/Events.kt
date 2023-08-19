package com.example.presenter.home

import com.example.core.base.BaseEvent
import com.example.core.enums.Sex

sealed class HomeEvent : BaseEvent {
    data class OnSexFilterClicked(val sex: Sex) : HomeEvent()
}


