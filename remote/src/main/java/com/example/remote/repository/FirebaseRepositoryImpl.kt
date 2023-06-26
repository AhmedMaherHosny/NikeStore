package com.example.remote.repository

import com.example.core.Constants.COLLECTION_OF_USERS
import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import com.example.remote.mappers.toAppUserDomain
import com.example.remote.mappers.toAppUserRemoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            val userDocument = usersCollection.document(appUserDomainModel.uid!!)
            userDocument.set(appUserDomainModel.toAppUserRemoteModel())
                .addOnSuccessListener {
                    continuation.resume(appUserDomainModel)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }

        }
}
