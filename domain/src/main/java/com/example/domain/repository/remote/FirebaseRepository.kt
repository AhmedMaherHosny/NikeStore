package com.example.domain.repository.remote

import com.example.domain.models.AppUserDomainModel

interface FirebaseRepository {
    suspend fun createUserByEmailAndPassword(email: String, password: String): AppUserDomainModel
    suspend fun addUserToFireStore(appUserDomainModel: AppUserDomainModel) : AppUserDomainModel
}