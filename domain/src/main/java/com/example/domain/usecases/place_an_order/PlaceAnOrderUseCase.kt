package com.example.domain.usecases.place_an_order

import com.example.domain.models.OrderDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class PlaceAnOrderUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(orderDomainModel: OrderDomainModel) =
        repository.placeAnOrder(orderDomainModel)
}