package com.example.domain.repository.remote

import com.example.domain.models.AppUserDomainModel

interface FirebaseRepository {
    suspend fun createUserByEmailAndPassword(email: String, password: String): AppUserDomainModel
    suspend fun addUserToFireStore(appUserDomainModel: AppUserDomainModel): AppUserDomainModel
    suspend fun setOnlineUser(id: String)
    suspend fun setOfflineUser(id: String)
    suspend fun loginUserByEmailAndPassword(email: String, password: String) : String
    suspend fun getUserDataFromServer(id: String) : AppUserDomainModel

}