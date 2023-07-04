package com.example.domain.usecases.login_user_by_email_and_password

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class LoginUserByEmailAndPasswordUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(email: String, password: String) : String =
        repository.loginUserByEmailAndPassword(email, password)
}