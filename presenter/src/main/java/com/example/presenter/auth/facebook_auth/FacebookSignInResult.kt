package com.example.presenter.auth.facebook_auth

import com.example.presenter.models.AppUserUiModel

data class FacebookSignInResult(
    val user: AppUserUiModel? = null,
    val error: String? = null
)