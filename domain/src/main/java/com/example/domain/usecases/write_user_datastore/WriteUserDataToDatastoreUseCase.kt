package com.example.domain.usecases.write_user_datastore

import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.local.DatastoreRepository
import javax.inject.Inject

class WriteUserDataToDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke(key: String, value: AppUserDomainModel) =
        repository.writeUserModel(key, value)
}