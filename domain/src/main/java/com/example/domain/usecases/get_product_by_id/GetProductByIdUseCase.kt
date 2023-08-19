package com.example.domain.usecases.get_product_by_id

import com.example.domain.models.ProductItemDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(id: String): ProductItemDomainModel =
        repository.getProduct(id)
}