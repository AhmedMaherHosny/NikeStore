package com.example.remote.mappers

import com.example.domain.models.AppUserDomainModel
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toAppUserDomain(): AppUserDomainModel {
    return AppUserDomainModel(
        id = this.uid,
        email = this.email,
    )
}