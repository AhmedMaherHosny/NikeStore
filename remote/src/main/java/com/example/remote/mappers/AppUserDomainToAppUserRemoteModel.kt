package com.example.remote.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.remote.models.AppUserRemoteModel

fun AppUserDomainModel.toAppUserRemoteModel(): AppUserRemoteModel {
    return AppUserRemoteModel(
        uid = uid,
        email = email,
        username = username,
        phone = phone,
        avatar = avatar
    )
}