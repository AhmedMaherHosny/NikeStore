package com.example.presenter.models

import androidx.annotation.Keep

@Keep
data class ProductSizeUiModel(
    val size: Float? = null,
    val availablePieces: Int = 0
)
