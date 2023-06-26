package com.example.presenter.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.presenter.models.AppUserUiModel

fun AppUserDomainModel.toAppUserUiModel(): AppUserUiModel {
    return AppUserUiModel(
        uid = uid,
        username = username,
        email = email,
        phone = phone,
        avatar = avatar
    )
}