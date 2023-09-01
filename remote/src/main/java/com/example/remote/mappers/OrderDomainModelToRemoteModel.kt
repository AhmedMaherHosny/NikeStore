package com.example.remote.mappers

import com.example.domain.models.OrderDomainModel
import com.example.remote.models.OrderRemoteModel

fun OrderDomainModel.toOrderRemoteModel(): OrderRemoteModel {
    return OrderRemoteModel(
        id = id,
        userId = userId,
        orders = orders.map {
            it.toOrderItemRemoteModel()
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