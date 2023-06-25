package com.example.core

import kotlinx.coroutines.flow.MutableSharedFlow

interface BaseEvent

interface BaseState

interface BaseViewState {
    val isRefreshing: Boolean
    val isNetworkError: Boolean
    val isLoading: Boolean
    val error: String?
    val errorEvents: MutableSharedFlow<StateErrorType>?
}

enum class StateErrorType {
    INITIAL, AUTHENTICATION_FAILED, NETWORK_ERROR, SERVER_IS_OFFLINE, UNKOWN
}
