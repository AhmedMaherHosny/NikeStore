package com.example.remote.repository

import com.example.core.Constants.COLLECTION_OF_USERS
import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import com.example.remote.mappers.toAppUserDomain
import com.example.remote.mappers.toAppUserDomainModel
import com.example.remote.mappers.toAppUserRemoteModel
import com.example.remote.models.AppUserRemoteModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : FirebaseRepository {

    override suspend fun createUserByEmailAndPassword(
        email: String,
        password: String
    ): AppUserDomainModel =
        suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) continuation.resume(firebaseUser.toAppUserDomain())
                        else continuation.resumeWithException(NullPointerException())
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }

    override suspend fun addUserToFireStore(appUserDomainModel: AppUserDomainModel) =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(appUserDomainModel.id!!)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        userDocument.set(appUserDomainModel.toAppUserRemoteModel())
                            .addOnSuccessListener {
                                continuation.resume(appUserDomainModel)
                            }
                            .addOnFailureListener { task ->
                                continuation.resumeWithException(task)
                            }
                    } else {
                        val appUserRemoteModel =
                            documentSnapshot.toObject(AppUserRemoteModel::class.java)
                        continuation.resume(appUserRemoteModel!!.toAppUserDomainModel())
                    }
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }

    override suspend fun setOnlineUser(id: String) {
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(id)
            userDocument.update("online", true)
                .addOnSuccessListener { task ->
                    continuation.resume(task)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }
    }

    override suspend fun setOfflineUser(id: String) {
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(id)
            val updates = hashMapOf<String, Any>(
                "online" to false,
                "lastSeen" to SimpleDateFormat(
                    "dd MMM yyyy hh:mm a",
                    Locale.getDefault()
                ).format(Timestamp.now().toDate())
            )
            userDocument.update(updates)
                .addOnSuccessListener { task ->
                    continuation.resume(task)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }
    }

    override suspend fun loginUserByEmailAndPassword(email: String, password: String): String =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    continuation.resume(result.user!!.uid)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }

    override suspend fun getUserDataFromServer(id: String): AppUserDomainModel =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(id)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        continuation.resumeWithException(NullPointerException())
                    }
                    val appUserRemoteModel =
                        documentSnapshot.toObject(AppUserRemoteModel::class.java)
                    continuation.resume(appUserRemoteModel!!.toAppUserDomainModel())
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }
}
