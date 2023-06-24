package com.example.core

interface BaseEvent

interface BaseState

interface BaseViewState {
    val isRefreshing: Boolean
    val isNetworkError: Boolean
    val isLoading: Boolean
}