package com.example.presenter.models

import com.example.core.enums.Sex

data class OrderItemUiModel(
    val productId: String? = null,
    val name: String? = null,
    val price: Float? = null,
    val photoUrl: String? = null,
    val colorCode: String? = null,
    var quantity: Int = 1,
    val colorName: String? = null,
    val size: Int? = null,
    val sex: Sex? = null,
)
