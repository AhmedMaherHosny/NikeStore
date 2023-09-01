package com.example.local.repository

import com.example.domain.models.AddressDomainModel
import com.example.domain.repository.local.RoomDbRepository
import com.example.local.db.dao.AddressDao
import com.example.local.db.entities.AddressEntity
import javax.inject.Inject

class RoomDbRepositoryImpl @Inject constructor(
    private val addressDao: AddressDao
) : RoomDbRepository {
    override suspend fun upsertAddress(addressDomainModel: AddressDomainModel) {
        val entity = AddressEntity(
            id = addressDomainModel.id ?: 0,
            addressLine1 = addressDomainModel.addressLine1,
            addressLine2 = addressDomainModel.addressLine2,
            phoneNumber = addressDomainModel.phoneNumber,
            zipCode = addressDomainModel.zipCode,
            country = addressDomainModel.country,
            city = addressDomainModel.city
        )
        addressDao.upsert(entity)
    }

    override suspend fun getAddress(): AddressDomainModel {
        val entity: AddressEntity? = addressDao.getAddress()
        if (entity == null) return AddressDomainModel()
        return AddressDomainModel(
            id = entity!!.id,
            addressLine1 = entity.addressLine1,
            addressLine2 = entity.addressLine2,
            phoneNumber = entity.phoneNumber,
            zipCode = entity.zipCode,
            country = entity.country,
            city = entity.city
        )
    }
}