package com.example.presenter.mappers

import com.example.domain.models.ProductSizeDomainModel
import com.example.presenter.models.ProductSizeUiModel

fun ProductSizeDomainModel.toProductSizeUiModel(): ProductSizeUiModel {
    return ProductSizeUiModel(
        size = size,
        availablePieces = availablePieces,
    )
}