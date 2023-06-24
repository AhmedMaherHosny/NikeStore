package com.example.presenter.auth.states

import com.example.core.BaseViewState
import com.example.core.validation.InputWrapper
import com.example.core.validation.ValidationType

data class RegisterViewState(
    override val isRefreshing: Boolean = false,
    override val isNetworkError: Boolean = false,
    override val isLoading: Boolean = false,
    val username: InputWrapper = InputWrapper(validationType = ValidationType.USERNAME),
    val email: InputWrapper = InputWrapper(validationType = ValidationType.EMAIL),
    val password: InputWrapper = InputWrapper(validationType = ValidationType.PASSWORD),
    val phoneNumber: InputWrapper = InputWrapper(validationType = ValidationType.PHONE)
) : BaseViewState
