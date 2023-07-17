package com.example.presenter.auth.facebook_auth

import android.app.Activity
import com.example.core.Constants.DEFAULT_AVATAR_URL
import com.example.presenter.models.AppUserUiModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class FacebookAuthUiClient(private val activity: Activity) {
    private val firebaseAuth = Firebase.auth
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    suspend fun signIn(): FacebookSignInResult {
        return try {
            suspendForResult()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            FacebookSignInResult(user = null, error = e.message)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun suspendForResult(): FacebookSignInResult {
        return suspendCancellableCoroutine { continuation ->
            val loginCallback = object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken.token
                    val credential = FacebookAuthProvider.getCredential(token)
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result.user?.run {
                                    AppUserUiModel(
                                        id = uid,
                                        name = displayName,
                                        avatar = photoUrl?.toString() ?: DEFAULT_AVATAR_URL,
                                        phone = phoneNumber,
                                        email = email
                                    )
                                }
                                val signInResult = FacebookSignInResult(user = user, error = null)
                                continuation.resume(signInResult) {}
                            } else {
                                val exception = task.exception
                                continuation.resumeWithException(
                                    exception ?: Exception("Failed to handle Facebook access token")
                                )
                            }
                        }
                }

                override fun onCancel() {
                    continuation.cancel()
                }

                override fun onError(error: FacebookException) {
                    continuation.resumeWithException(error)
                }
            }

            LoginManager.getInstance().registerCallback(callbackManager, loginCallback)
            LoginManager.getInstance()
                .logInWithReadPermissions(activity, listOf("public_profile"))

            continuation.invokeOnCancellation {
                LoginManager.getInstance().unregisterCallback(callbackManager)
            }
        }
    }


}
