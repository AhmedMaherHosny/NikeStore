package com.example.presenter.auth.states

import com.example.core.base.BaseViewState
import com.example.core.base.StateErrorType
import com.example.core.validation.InputWrapper
import com.example.core.validation.ValidationType
import kotlinx.coroutines.flow.MutableSharedFlow

data class RegisterViewState(
    override val isRefreshing: Boolean = false,
    override val isNetworkError: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    override val errorEvents: MutableSharedFlow<StateErrorType>? = null,
    val username: InputWrapper = InputWrapper(validationType = ValidationType.USERNAME),
    val email: InputWrapper = InputWrapper(validationType = ValidationType.EMAIL),
    val password: InputWrapper = InputWrapper(validationType = ValidationType.PASSWORD),
    val phoneNumber: InputWrapper = InputWrapper(validationType = ValidationType.PHONE)
) : BaseViewState
