package com.example.domain.usecases.update_user_in_firestore

import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class UpdateUserInFireStoreUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(appUserDomainModel: AppUserDomainModel) = repository.updateUserInFireStore(appUserDomainModel)
}