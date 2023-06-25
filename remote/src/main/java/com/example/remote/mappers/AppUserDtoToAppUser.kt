package com.example.remote.mappers

import com.example.domain.models.AppUserDomainModel
import com.example.remote.dto.AppUserDtoModel

fun AppUserDtoModel.toAppUser(): AppUserDomainModel {
    return AppUserDomainModel(
        username = username,
        email = email,
        password = password,
        phone = phone,
        avatar = avatar
    )
}