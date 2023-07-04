package com.example.domain.models

import com.example.core.Constants.DEFAULT_AVATAR_URL
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class AppUserDomainModel(
    val id: String? = null,
    val name: String? = null,
    val password: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String = DEFAULT_AVATAR_URL,
    val isAdmin: Boolean = false,
)
