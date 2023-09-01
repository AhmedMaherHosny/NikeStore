package com.example.remote.mappers

import com.example.domain.models.ProductColorSizeInformationDomainModel
import com.example.remote.models.ProductColorSizeInformationRemoteModel

fun ProductColorSizeInformationRemoteModel.toProductColorSizeInformationDomainModel(): ProductColorSizeInformationDomainModel {
    return ProductColorSizeInformationDomainModel(
        colorName = colorName,
        colorCode = colorCode,
        photoUrl = photoUrl,
        sizeDomainModel = sizeRemoteModel.map {
            it.toProductSizeDomainModel()
        }
    )
}