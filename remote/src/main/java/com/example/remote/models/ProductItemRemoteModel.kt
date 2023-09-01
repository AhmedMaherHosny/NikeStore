package com.example.remote.models

import androidx.annotation.Keep
import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex

@Keep
data class ProductItemRemoteModel(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Float? = null,
    val peopleRatedCounter: Long = 0,
    val rate: Long = 0,
    val sex: Sex,
    val productCategory: ProductCategory,
    val subCategory: Any,
    val colorSizeInformation: List<ProductColorSizeInformationRemoteModel> = listOf(),
)
