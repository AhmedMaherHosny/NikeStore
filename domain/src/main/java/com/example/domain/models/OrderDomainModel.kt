package com.example.domain.models

import androidx.annotation.Keep
import com.example.core.enums.OrderStatus

@Keep
data class OrderDomainModel(
    val id: String? = null,
    var userId: String? = null,
    val orders: List<OrderItemDomainModel> = listOf(),
    val total: Float? = 0f,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val phoneNumber: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val city: String? = null,
    val orderStatus: OrderStatus? = null
)
