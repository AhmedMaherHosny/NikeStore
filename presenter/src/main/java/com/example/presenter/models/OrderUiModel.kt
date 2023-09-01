package com.example.presenter.models

import androidx.annotation.Keep
import com.example.core.enums.OrderStatus

@Keep
data class OrderUiModel(
    val id: String? = null,
    val orders: List<OrderItemUiModel> = listOf(),
    val total: Float? = 0f,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val phoneNumber: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val city: String? = null,
    val orderStatus: OrderStatus? = null
)
