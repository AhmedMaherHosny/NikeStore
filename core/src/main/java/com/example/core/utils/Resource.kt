package com.example.core.utils

import com.example.core.base.StateErrorType

sealed class Resource<T>(val data: T? = null, val message: String? = null, val errorType: StateErrorType? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, errorType: StateErrorType? = null) :
        Resource<T>(data, message, errorType)

    class Loading<T>(data: T? = null) : Resource<T>(data)
}