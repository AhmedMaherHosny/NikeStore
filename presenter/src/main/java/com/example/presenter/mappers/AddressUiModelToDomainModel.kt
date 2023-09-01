package com.example.presenter.mappers

import com.example.domain.models.AddressDomainModel
import com.example.presenter.models.AddressUiModel

fun AddressUiModel.toAddressDomainModel(): AddressDomainModel {
    return AddressDomainModel(
        id = id,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        phoneNumber = phoneNumber,
        zipCode = zipCode,
        country = country,
        city = city
    )
}