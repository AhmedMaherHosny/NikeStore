package com.example.remote.mappers

import com.example.core.Constants.DEFAULT_AVATAR_URL
import com.example.domain.models.AppUserDomainModel
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toAppUserDomain(): AppUserDomainModel {
    return AppUserDomainModel(
        uid = this.uid,
        email = this.email,
        username = this.displayName,
        phone = this.phoneNumber,
        avatar = this.photoUrl?.toString() ?: DEFAULT_AVATAR_URL
    )
}