package com.example.domain.usecases.create_user_by_email_password

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class CreateUserByEmailAndPasswordUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.createUserByEmailAndPassword(email, password)

}