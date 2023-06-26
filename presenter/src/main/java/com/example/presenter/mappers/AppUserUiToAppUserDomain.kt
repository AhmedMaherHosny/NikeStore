package com.example.presenter.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.presenter.models.AppUserUiModel

fun AppUserUiModel.toAppUserDomainModel(): AppUserDomainModel {
    return AppUserDomainModel(
        uid = uid,
        username = username,
        email = email,
        phone = phone,
        avatar = avatar
    )
}