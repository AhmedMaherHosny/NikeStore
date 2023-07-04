package com.example.remote.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.remote.models.AppUserRemoteModel

fun AppUserRemoteModel.toAppUserDomainModel(): AppUserDomainModel {
    return AppUserDomainModel(
        id = id,
        email = email,
        name = name,
        password = password,
        phone = phone,
        avatar = avatar,
        isAdmin = admin
    )
}