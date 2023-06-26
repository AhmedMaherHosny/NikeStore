package com.example.local.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.local.models.AppUserLocalModel

fun AppUserLocalModel.toAppUserDomain(): AppUserDomainModel {
    return AppUserDomainModel(
        uid = uid,
        username = username,
        email = email,
        phone = phone,
        avatar = avatar
    )
}