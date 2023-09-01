package com.example.remote.models

import androidx.annotation.Keep

@Keep
data class ProductSizeRemoteModel(
    val size: Float? = null,
    val availablePieces: Int = 0
)
