package com.example.local.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.local.models.AppUserLocalModel

fun AppUserLocalModel.toAppUserDomain(): AppUserDomainModel {
    return AppUserDomainModel(
        id = id,
        name = name,
        email = email,
        phone = phone,
        avatar = avatar,
        isAdmin = isAdmin
    )
}