package com.example.presenter.auth.google_auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.core.Constants.DEFAULT_AVATAR_URL
import com.example.core.Constants.WEB_CLIENT_ID_GOOGLE
import com.example.presenter.models.AppUserUiModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
) {

    private val firebaseAuth = Firebase.auth
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): GoogleSignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = firebaseAuth.signInWithCredential(googleCredentials).await().user
            GoogleSignInResult(
                user = user?.run {
                    AppUserUiModel(
                        id = uid,
                        name = displayName,
                        avatar = photoUrl?.toString() ?: DEFAULT_AVATAR_URL,
                        phone = phoneNumber,
                        email = email
                    )
                },
                error = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            GoogleSignInResult(
                user = null,
                error = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): AppUserUiModel? = firebaseAuth.currentUser?.run {
        AppUserUiModel(
            id = uid,
            name = displayName,
            avatar = photoUrl?.toString() ?: DEFAULT_AVATAR_URL,
            phone = phoneNumber,
            email = email
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID_GOOGLE)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}