package com.example.presenter.mappers

import com.example.domain.models.ProductItemDomainModel
import com.example.presenter.models.ProductItemUiModel

fun ProductItemDomainModel.toProductItemUiModel(): ProductItemUiModel {
    return ProductItemUiModel(
        id = id,
        name = name,
        description = description,
        price = price,
        isSaved = isSaved,
        isInShoppingBag = isInShoppingBag,
        peopleRatedCounter = peopleRatedCounter,
        rate = rate,
        sex = sex,
        productCategory = productCategory,
        subCategory = subCategory,
        colorSizeInformation = colorSizeInformation.map {
            it.toProductColorSizeInformationUiModel()
        }
    )
}