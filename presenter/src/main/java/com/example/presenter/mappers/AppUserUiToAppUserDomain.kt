package com.example.presenter.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.presenter.models.AppUserUiModel

fun AppUserUiModel.toAppUserDomainModel(): AppUserDomainModel {
    return AppUserDomainModel(
        username = username,
        email = email,
        password = password,
        phone = phone,
        avatar = avatar
    )
}