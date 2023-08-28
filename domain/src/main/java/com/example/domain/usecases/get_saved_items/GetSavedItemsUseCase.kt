package com.example.domain.usecases.get_saved_items

import com.example.domain.models.ProductItemDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetSavedItemsUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String): List<ProductItemDomainModel> =
        repository.getSavedItems(userId)
}