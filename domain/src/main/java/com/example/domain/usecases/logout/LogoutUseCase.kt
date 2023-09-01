package com.example.domain.usecases.logout

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke()  = repository.logout()
}