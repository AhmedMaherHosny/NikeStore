package com.example.presenter.models

import com.example.core.Constants.DEFAULT_AVATAR_URL

data class AppUserUiModel(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String = DEFAULT_AVATAR_URL,
    val isAdmin : Boolean = false
)
