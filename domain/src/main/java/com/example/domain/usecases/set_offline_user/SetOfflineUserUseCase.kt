package com.example.domain.usecases.set_offline_user

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class SetOfflineUserUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(id: String) = repository.setOfflineUser(id)
}