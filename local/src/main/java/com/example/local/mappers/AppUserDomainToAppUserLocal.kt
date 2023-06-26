package com.example.local.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.local.models.AppUserLocalModel

fun AppUserDomainModel.toAppUserLocal(): AppUserLocalModel {
    return AppUserLocalModel(
        id = id,
        name = name,
        email = email,
        phone = phone,
        avatar = avatar,
        isAdmin = isAdmin
    )
}