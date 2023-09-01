package com.example.remote.mappers

import com.example.domain.models.ProductSizeDomainModel
import com.example.remote.models.ProductSizeRemoteModel

fun ProductSizeRemoteModel.toProductSizeDomainModel(): ProductSizeDomainModel {
    return ProductSizeDomainModel(
        size = size,
        availablePieces = availablePieces,
    )
}