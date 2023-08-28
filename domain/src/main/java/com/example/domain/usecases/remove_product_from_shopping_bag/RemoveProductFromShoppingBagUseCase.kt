package com.example.domain.usecases.remove_product_from_shopping_bag

import com.example.domain.models.OrderItemDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class RemoveProductFromShoppingBagUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, orderItemDomainModel: OrderItemDomainModel) =
        repository.removeProductFromShoppingBag(userId, orderItemDomainModel)
}