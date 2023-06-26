package com.example.local.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.local.models.AppUserLocalModel

fun AppUserDomainModel.toAppUserLocal(): AppUserLocalModel {
    return AppUserLocalModel(
        uid = uid,
        username = username,
        email = email,
        phone = phone,
        avatar = avatar
    )
}