package com.example.remote.models

import androidx.annotation.Keep

@Keep
data class ProductColorSizeInformationRemoteModel(
    val colorName: String? = null,
    val colorCode: String? = null,
    val photoUrl: String? = null,
    val sizeRemoteModel: List<ProductSizeRemoteModel> = listOf()
)
