package com.example.presenter.address.states

import com.example.core.validation.InputWrapper

data class AddressViewState(
    var id: Long? = null,
    val addressLine1: InputWrapper = InputWrapper(),
    val addressLine2: InputWrapper = InputWrapper(),
    val phoneNumber: InputWrapper = InputWrapper(),
    val zipCode: InputWrapper = InputWrapper(),
    val country: InputWrapper = InputWrapper(),
    val city: InputWrapper = InputWrapper(),
)
