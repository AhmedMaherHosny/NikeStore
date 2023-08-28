package com.example.domain.repository.remote

import com.example.domain.models.AppUserDomainModel
import com.example.domain.models.OrderDomainModel
import com.example.domain.models.OrderItemDomainModel
import com.example.domain.models.ProductItemDomainModel

interface FirebaseRepository {
    suspend fun createUserByEmailAndPassword(email: String, password: String): AppUserDomainModel
    suspend fun addUserToFireStore(appUserDomainModel: AppUserDomainModel): AppUserDomainModel
    suspend fun setOnlineUser(id: String)
    suspend fun setOfflineUser(id: String)
    suspend fun loginUserByEmailAndPassword(email: String, password: String): String
    suspend fun getUserDataFromServer(id: String): AppUserDomainModel
    suspend fun getProducts(userId: String): List<ProductItemDomainModel>
    suspend fun getProduct(userId: String, productId: String): ProductItemDomainModel
    suspend fun saveProduct(userId: String, productId: String)
    suspend fun unSaveProduct(userId: String, productId: String)
    suspend fun getSavedItems(userId: String): List<ProductItemDomainModel>
    suspend fun placeAnOrder(orderDomainModel: OrderDomainModel)
    suspend fun addProductToShoppingBag(userId: String, orderItemDomainModel: OrderItemDomainModel)
    suspend fun removeProductFromShoppingBag(
        userId: String,
        orderItemDomainModel: OrderItemDomainModel
    )

    suspend fun getProductsFromShoppingBag(userId: String): List<OrderItemDomainModel>
}