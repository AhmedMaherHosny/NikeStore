package com.example.domain.usecases.read_user_datastore

import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.local.DatastoreRepository
import javax.inject.Inject

class ReadUserDataFromDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke(key: String): AppUserDomainModel? {
        return repository.readUserModel(key)
    }
}