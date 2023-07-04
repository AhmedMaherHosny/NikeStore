package com.example.remote.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.remote.models.AppUserRemoteModel

fun AppUserDomainModel.toAppUserRemoteModel(): AppUserRemoteModel {
    return AppUserRemoteModel(
        id = id,
        email = email,
        name = name,
        password = password,
        phone = phone,
        avatar = avatar
    )
}