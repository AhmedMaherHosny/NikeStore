package com.example.presenter.auth.google_auth

import com.example.presenter.models.AppUserUiModel

data class GoogleSignInResult(
    val user: AppUserUiModel? = null,
    val error: String? = null
)