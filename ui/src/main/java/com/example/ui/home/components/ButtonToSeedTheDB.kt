package com.example.ui.home.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.core.Constants.COLLECTION_OF_PRODUCTS
import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex
import com.example.core.enums.ShoeCategory
import com.example.presenter.models.ProductColorSizeInformationUiModel
import com.example.presenter.models.ProductItemUiModel
import com.example.presenter.models.ProductSizeUiModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun ButtonToSeedTheDbForMen() {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val product = ProductItemUiModel(
        name = "Air Jordan",
        description = "this shoe is best one to run in it this is so soft and comfortable and this is original from the nike it self not made in china.",
        price = 40f,
        peopleRatedCounter = 0,
        rate = 0,
        sex = Sex.MEN,
        productCategory = ProductCategory.SHOE,
        subCategory = ShoeCategory.JORDAN,
        colorSizeInformation = listOf(
            ProductColorSizeInformationUiModel(
                colorName = "Red",
                colorCode = "0xFF0000",
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/nike-store-84533.appspot.com/o/image_2023-08-11_204005420-removebg-preview.png?alt=media&token=ca435bd2-7303-454b-8623-9abc71fa6997",
                sizeUiModel = listOf(
                    ProductSizeUiModel(
                        size = 40f,
                        availablePieces = 1
                    )
                )
            )
        )
    )
    val product1 = ProductItemUiModel(
        name = "Air Jordan",
        description = "this shoe is best one to run in it this is so soft and comfortable and this is original from the nike it self not made in china.",
        price = 1999.99f,
        peopleRatedCounter = 5,
        rate = 25,
        sex = Sex.MEN,
        productCategory = ProductCategory.SHOE,
        subCategory = ShoeCategory.JORDAN,
        colorSizeInformation = listOf(
            ProductColorSizeInformationUiModel(
                colorName = "Blue",
                colorCode = "0xFF5780D9",
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/nike-store-84533.appspot.com/o/image_2023-08-11_203915259-removebg-preview.png?alt=media&token=722b56e0-abb0-4be6-be08-6a2720d5601c",
                sizeUiModel = listOf(
                    ProductSizeUiModel(
                        size = 44f,
                        availablePieces = 3
                    )
                )
            )
        )
    )
    Button(onClick = {
        val newProductRef = firebaseFirestore.collection(COLLECTION_OF_PRODUCTS).document()
        newProductRef.set(product).addOnSuccessListener {
            val documentId = newProductRef.id
            val updatedProduct = product.copy(id = documentId)
            newProductRef.set(updatedProduct, SetOptions.merge())
        }
    }) {
        Text(text = "Add Men Item")
    }
}

@Composable
fun ButtonToSeedTheDbForWomen() {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val product = ProductItemUiModel(
        name = "Air Jordan",
        description = "this shoe is best one to run in it this is so soft and comfortable and this is original from the nike it self not made in china.",
        price = 399.5f,
        peopleRatedCounter = 0,
        rate = 0,
        sex = Sex.WOMEN,
        productCategory = ProductCategory.SHOE,
        subCategory = ShoeCategory.JORDAN,
        colorSizeInformation = listOf(
            ProductColorSizeInformationUiModel(
                colorName = "Violet",
                colorCode = "0xFF7F00FF",
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/nike-store-84533.appspot.com/o/image_2023-08-11_204418688-removebg-preview.png?alt=media&token=884c10c6-77f0-480e-b6e1-9eb099e755a0",
                sizeUiModel = listOf(
                    ProductSizeUiModel(
                        size = 39f,
                        availablePieces = 5
                    )
                )
            )
        )
    )
    val product1 = ProductItemUiModel(
        name = "Air Jordan",
        description = "this shoe is best one to run in it this is so soft and comfortable and this is original from the nike it self not made in china.",
        price = 999.99f,
        peopleRatedCounter = 0,
        rate = 0,
        sex = Sex.WOMEN,
        productCategory = ProductCategory.SHOE,
        subCategory = ShoeCategory.JORDAN,
        colorSizeInformation = listOf(
            ProductColorSizeInformationUiModel(
                colorName = "White",
                colorCode = "0xFFFFFFFF",
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/nike-store-84533.appspot.com/o/image_2023-08-11_204620143-removebg-preview.png?alt=media&token=6655903f-5163-43e3-a90d-80c3e79f7310",
                sizeUiModel = listOf(
                    ProductSizeUiModel(
                        size = 42f,
                        availablePieces = 5
                    )
                )
            )
        )
    )
    Button(onClick = {
        val newProductRef = firebaseFirestore.collection(COLLECTION_OF_PRODUCTS).document()
        newProductRef.set(product).addOnSuccessListener {
            val documentId = newProductRef.id
            val updatedProduct = product.copy(id = documentId)
            newProductRef.set(updatedProduct, SetOptions.merge())
        }
    }) {
        Text(text = "Add Women Item")
    }
}