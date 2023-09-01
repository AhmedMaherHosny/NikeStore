package com.example.domain.models

import androidx.annotation.Keep

@Keep
data class ProductSizeDomainModel(
    val size: Float? = null,
    val availablePieces: Int = 0
)
