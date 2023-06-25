package com.example.domain.usecases.delete_user_datastore

import com.example.domain.repository.local.DatastoreRepository
import javax.inject.Inject

class DeleteUserDataFromDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke(key: String) {
        repository.deleteUserModel(key)
    }
}