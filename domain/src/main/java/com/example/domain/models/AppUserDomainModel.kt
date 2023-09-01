package com.example.domain.models

import androidx.annotation.Keep
import com.example.core.Constants.DEFAULT_AVATAR_URL

@Keep
data class AppUserDomainModel(
    val id: String? = null,
    val name: String? = null,
    val password: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String = DEFAULT_AVATAR_URL,
    val isAdmin: Boolean = false,
)
