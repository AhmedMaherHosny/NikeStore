package com.example.domain.usecases.upsert_address

import com.example.domain.models.AddressDomainModel
import com.example.domain.repository.local.RoomDbRepository
import javax.inject.Inject

class UpsertAddressUseCase @Inject constructor(
    private val repository: RoomDbRepository
) {
    suspend operator fun invoke(addressDomainModel: AddressDomainModel) =
        repository.upsertAddress(addressDomainModel)
}