package com.example.domain.models

import com.example.core.Constants.DEFAULT_AVATAR_URL

data class AppUserDomainModel(
    val uid: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String = DEFAULT_AVATAR_URL
)
