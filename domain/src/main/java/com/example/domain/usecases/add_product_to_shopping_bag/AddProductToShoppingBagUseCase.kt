package com.example.domain.usecases.add_product_to_shopping_bag

import com.example.domain.models.OrderItemDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class AddProductToShoppingBagUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, orderItemDomainModel: OrderItemDomainModel) =
        repository.addProductToShoppingBag(userId, orderItemDomainModel)
}