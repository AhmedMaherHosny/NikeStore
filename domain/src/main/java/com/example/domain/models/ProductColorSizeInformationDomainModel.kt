package com.example.domain.models

data class ProductColorSizeInformationDomainModel(
    val colorName: String? = null,
    val colorCode: String? = null,
    val photoUrl: String? = null,
    val sizeDomainModel: List<ProductSizeDomainModel> = listOf()
)
