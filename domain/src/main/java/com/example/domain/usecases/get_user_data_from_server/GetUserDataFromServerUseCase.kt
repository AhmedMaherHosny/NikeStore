package com.example.domain.usecases.get_user_data_from_server

import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetUserDataFromServerUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(id: String): AppUserDomainModel =
        repository.getUserDataFromServer(id)
}