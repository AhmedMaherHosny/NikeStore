package com.example.domain.usecases.set_online_user

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class SetOnlineUserUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(id: String) = repository.setOnlineUser(id)
}