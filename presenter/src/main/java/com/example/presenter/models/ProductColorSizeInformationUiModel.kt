package com.example.presenter.models

import com.example.domain.models.ProductSizeDomainModel

data class ProductColorSizeInformationUiModel(
    val colorName: String? = null,
    val colorCode: String? = null,
    val photoUrl: String? = null,
    val sizeUiModel: List<ProductSizeUiModel> = listOf()
)
