package com.example.remote.models

import androidx.annotation.Keep
import com.example.core.Constants
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Keep
data class AppUserRemoteModel(
    val id: String? = null,
    val name: String? = null,
    val password: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String = Constants.DEFAULT_AVATAR_URL,
    val admin: Boolean = false,
    val online: Boolean = true,
    val lastSeen: String? = null,
    val savedProducts: List<String> = listOf(),
    val shoppingBag: List<String> = listOf(),
    val createdTimeStamp: Long = Timestamp.now().toDate().time,
    val createdFormattedDate: String = SimpleDateFormat(
        "dd MMM yyyy hh:mm a",
        Locale.getDefault()
    ).format(Timestamp.now().toDate())

)