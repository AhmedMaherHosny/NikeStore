package com.example.remote.models

import androidx.annotation.Keep
import com.example.core.enums.Sex

@Keep
data class OrderItemRemoteModel(
    val productId: String? = null,
    val name: String? = null,
    val price: Float? = null,
    val photoUrl: String? = null,
    val colorCode: String? = null,
    val quantity: Int = 1,
    val colorName: String? = null,
    val size: Int? = null,
    val sex: Sex? = null,
)
