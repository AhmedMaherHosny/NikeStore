package com.example.local.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.local.models.AppUserLocalModel

fun AppUserDomainModel.toAppUserLocal(): AppUserLocalModel {
    return AppUserLocalModel(
        username = username,
        email = email,
        password = password,
        phone = phone,
        avatar = avatar
    )
}