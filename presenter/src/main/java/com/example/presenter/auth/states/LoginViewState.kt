package com.example.presenter.auth.states

import com.example.core.BaseViewState
import com.example.core.validation.InputWrapper
import com.example.core.validation.ValidationType
import kotlinx.coroutines.flow.MutableSharedFlow
import com.example.core.StateErrorType

data class LoginViewState(
    override val isRefreshing: Boolean = false,
    override val isNetworkError: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    override val errorEvents: MutableSharedFlow<StateErrorType>? = null,
    val email: InputWrapper = InputWrapper(validationType = ValidationType.EMAIL),
    val password: InputWrapper = InputWrapper(
        validationType = ValidationType.PASSWORD,
        isLogin = true
    ),
) : BaseViewState
