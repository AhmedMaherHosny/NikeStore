package com.example.domain.usecases.unsave_product

import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class UnSaveProductUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, productId: String) =
        repository.unSaveProduct(userId, productId)
}