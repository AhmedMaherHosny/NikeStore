package com.example.domain.models

import com.example.core.enums.Sex
import com.example.core.enums.ProductCategory

data class ProductItemDomainModel(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Float? = null,
    val isSaved : Boolean = false,
    val isInShoppingBag : Boolean = false,
    val peopleRatedCounter: Long = 0,
    val rate: Long = 0,
    val sex: Sex,
    val productCategory: ProductCategory,
    val subCategory: Any,
    val colorSizeInformation: List<ProductColorSizeInformationDomainModel> = listOf(),
)
