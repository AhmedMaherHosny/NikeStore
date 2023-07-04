package com.example.domain.usecases.add_user_to_firestore

import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class AddUserToFireStoreUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(appUserDomainModel: AppUserDomainModel) =
        repository.addUserToFireStore(appUserDomainModel)
}