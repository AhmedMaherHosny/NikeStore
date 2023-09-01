package com.example.presenter.mappers

import com.example.domain.models.OrderItemDomainModel
import com.example.presenter.models.OrderItemUiModel

fun OrderItemDomainModel.toOrderItemUiModel(): OrderItemUiModel {
    return OrderItemUiModel(
        productId = productId,
        name = name,
        price = price,
        photoUrl = photoUrl,
        colorCode = colorCode,
        quantity = quantity,
        colorName = colorName,
        size = size,
        sex = sex,
    )
}