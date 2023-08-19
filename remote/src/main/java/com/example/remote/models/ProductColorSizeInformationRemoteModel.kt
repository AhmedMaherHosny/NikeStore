package com.example.remote.models

data class ProductColorSizeInformationRemoteModel(
    val colorName: String? = null,
    val colorCode: String? = null,
    val photoUrl: String? = null,
    val sizeRemoteModel: List<ProductSizeRemoteModel> = listOf()
)
