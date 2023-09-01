package com.example.presenter.mappers

import com.example.domain.models.OrderDomainModel
import com.example.presenter.models.OrderUiModel

fun OrderDomainModel.toOrderUiModel(): OrderUiModel {
    return OrderUiModel(
        id = id,
        orders = orders.map {
            it.toOrderItemUiModel()
        },
        total = total,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        phoneNumber = phoneNumber,
        zipCode = zipCode,
        country = country,
        city = city,
        orderStatus = orderStatus
    )
}