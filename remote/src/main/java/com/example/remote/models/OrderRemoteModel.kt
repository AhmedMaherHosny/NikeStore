package com.example.remote.models

import androidx.annotation.Keep
import com.example.core.enums.OrderStatus
import com.example.core.enums.Sex

@Keep
data class OrderRemoteModel(
    val id: String? = null,
    val userId: String? = null,
    val orders : List<OrderItemRemoteModel> = listOf(),
    val total: Float? = 0f,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val phoneNumber: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val city: String? = null,
    val orderStatus: OrderStatus? = null
)
