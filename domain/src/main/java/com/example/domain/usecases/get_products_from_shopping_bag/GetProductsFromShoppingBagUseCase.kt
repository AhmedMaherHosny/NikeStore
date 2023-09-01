package com.example.domain.usecases.get_products_from_shopping_bag

import com.example.domain.models.OrderItemDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class GetProductsFromShoppingBagUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String): List<OrderItemDomainModel> =
        repository.getProductsFromShoppingBag(userId)
}