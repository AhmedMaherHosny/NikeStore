package com.example.domain.usecases.get_address

import com.example.domain.models.AddressDomainModel
import com.example.domain.repository.local.RoomDbRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repository: RoomDbRepository
) {
    suspend operator fun invoke() : AddressDomainModel = repository.getAddress()
}