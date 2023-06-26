package com.example.domain.models

import com.example.core.PeopleCategory
import com.example.core.ProductCategory

data class ProductItemDomainModel(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Float? = null,
    val peopleRatedCounter: Long = 0,
    val rate: Long = 0,
    val peopleCategory: PeopleCategory,
    val productCategory: ProductCategory,
    val subCategory: Any,
    val colorSizeInformation: List<ProductColorSizeInformationDomainModel> = listOf(),
)
