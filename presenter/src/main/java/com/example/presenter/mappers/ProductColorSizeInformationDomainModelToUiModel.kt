package com.example.presenter.mappers

import com.example.domain.models.ProductColorSizeInformationDomainModel
import com.example.presenter.models.ProductColorSizeInformationUiModel

fun ProductColorSizeInformationDomainModel.toProductColorSizeInformationUiModel(): ProductColorSizeInformationUiModel {
    return ProductColorSizeInformationUiModel(
        colorName = colorName,
        colorCode = colorCode,
        photoUrl = photoUrl,
        sizeUiModel = sizeDomainModel.map {
            it.toProductSizeUiModel()
        }
    )
}