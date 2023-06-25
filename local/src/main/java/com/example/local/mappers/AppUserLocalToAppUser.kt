package com.example.local.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.local.models.AppUserLocalModel

fun AppUserLocalModel.toAppUser(): AppUserDomainModel {
    return AppUserDomainModel(
        username = username,
        email = email,
        password = password,
        phone = phone,
        avatar = avatar
    )
}