package com.example.domain.models

import androidx.annotation.Keep

@Keep
data class AddressDomainModel(
    val id: Long? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val phoneNumber: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val city: String? = null,
)
