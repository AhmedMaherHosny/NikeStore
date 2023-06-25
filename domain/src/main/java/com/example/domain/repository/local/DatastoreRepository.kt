package com.example.domain.repository.local

import com.example.domain.models.AppUserDomainModel

interface DatastoreRepository {
    suspend fun writeUserModel(key: String, value: AppUserDomainModel)
    suspend fun readUserModel(key: String): AppUserDomainModel?
    suspend fun deleteUserModel(key: String)
}