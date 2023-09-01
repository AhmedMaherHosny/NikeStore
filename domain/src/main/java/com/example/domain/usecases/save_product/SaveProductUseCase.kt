package com.example.domain.usecases.save_product

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, productId: String) =
        repository.saveProduct(userId, productId)
}