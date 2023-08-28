package com.example.domain.repository.local

import com.example.domain.models.AddressDomainModel
import com.example.domain.models.ProductItemDomainModel

interface RoomDbRepository {
    suspend fun upsertAddress(addressDomainModel: AddressDomainModel)
    suspend fun getAddress() : AddressDomainModel
}