package com.example.presenter.models

import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex

data class ProductItemUiModel(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Float? = null,
    var isSaved : Boolean = false,
    var isInShoppingBag : Boolean = false,
    val peopleRatedCounter: Long = 0,
    val rate: Long = 0,
    val sex: Sex,
    val productCategory: ProductCategory,
    val subCategory: Any,
    val colorSizeInformation: List<ProductColorSizeInformationUiModel> = listOf(),
)
