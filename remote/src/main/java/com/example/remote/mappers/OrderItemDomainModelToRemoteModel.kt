package com.example.remote.mappers

import com.example.domain.models.OrderItemDomainModel
import com.example.remote.models.OrderItemRemoteModel

fun OrderItemDomainModel.toOrderItemRemoteModel(): OrderItemRemoteModel {
    return OrderItemRemoteModel(
        productId = productId,
        name = name,
        price = price,
        photoUrl = photoUrl,
        colorCode = colorCode,
        quantity = quantity,
        colorName = colorName,
        size = size,
        sex = sex
    )
}