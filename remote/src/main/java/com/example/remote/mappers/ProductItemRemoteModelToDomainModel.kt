package com.example.remote.mappers

import com.example.domain.models.ProductItemDomainModel
import com.example.remote.models.ProductItemRemoteModel

fun ProductItemRemoteModel.toProductItemDomainModel(): ProductItemDomainModel {
    return ProductItemDomainModel(
        id = id,
        name = name,
        description = description,
        price = price,
        peopleRatedCounter = peopleRatedCounter,
        rate = rate,
        sex = sex,
        productCategory = productCategory,
        subCategory = subCategory,
        colorSizeInformation = colorSizeInformation.map {
            it.toProductColorSizeInformationDomainModel()
        }
    )
}