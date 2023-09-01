package com.example.remote.repository

import com.example.core.Constants.COLLECTION_OF_ORDERS
import com.example.core.Constants.COLLECTION_OF_PRODUCTS
import com.example.core.Constants.COLLECTION_OF_USERS
import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex
import com.example.domain.models.AppUserDomainModel
import com.example.domain.models.OrderDomainModel
import com.example.domain.models.OrderItemDomainModel
import com.example.domain.models.ProductColorSizeInformationDomainModel
import com.example.domain.models.ProductItemDomainModel
import com.example.domain.models.ProductSizeDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import com.example.remote.mappers.toAppUserDomain
import com.example.remote.mappers.toAppUserDomainModel
import com.example.remote.mappers.toAppUserRemoteModel
import com.example.remote.mappers.toOrderItemRemoteModel
import com.example.remote.mappers.toOrderRemoteModel
import com.example.remote.models.AppUserRemoteModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : FirebaseRepository {

    override suspend fun createUserByEmailAndPassword(
        email: String,
        password: String
    ): AppUserDomainModel =
        suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) continuation.resume(firebaseUser.toAppUserDomain())
                        else continuation.resumeWithException(NullPointerException())
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }

    override suspend fun addUserToFireStore(appUserDomainModel: AppUserDomainModel) =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(appUserDomainModel.id!!)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        userDocument.set(appUserDomainModel.toAppUserRemoteModel())
                            .addOnSuccessListener {
                                continuation.resume(appUserDomainModel)
                            }
                            .addOnFailureListener { task ->
                                continuation.resumeWithException(task)
                            }
                    } else {
                        val appUserRemoteModel =
                            documentSnapshot.toObject(AppUserRemoteModel::class.java)
                        continuation.resume(appUserRemoteModel!!.toAppUserDomainModel())
                    }
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }

    override suspend fun setOnlineUser(id: String) {
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(id)
            userDocument.update("online", true)
                .addOnSuccessListener { task ->
                    continuation.resume(task)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }
    }

    override suspend fun setOfflineUser(id: String) {
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(id)
            val updates = hashMapOf<String, Any>(
                "online" to false,
                "lastSeen" to SimpleDateFormat(
                    "dd MMM yyyy hh:mm a",
                    Locale.getDefault()
                ).format(Timestamp.now().toDate())
            )
            userDocument.update(updates)
                .addOnSuccessListener { task ->
                    continuation.resume(task)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }
    }

    override suspend fun loginUserByEmailAndPassword(email: String, password: String): String =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    continuation.resume(result.user!!.uid)
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }

    override suspend fun getUserDataFromServer(id: String): AppUserDomainModel =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(id)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        continuation.resumeWithException(NullPointerException())
                    }
                    val appUserRemoteModel =
                        documentSnapshot.toObject(AppUserRemoteModel::class.java)
                    continuation.resume(appUserRemoteModel!!.toAppUserDomainModel())
                }
                .addOnFailureListener { task ->
                    continuation.resumeWithException(task)
                }
        }

    override suspend fun getProducts(userId: String): List<ProductItemDomainModel> =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(userId)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.data
                    val savedItems = userData?.get("savedProducts") as? List<String> ?: emptyList()
                    val shoppingBagList =
                        documentSnapshot.get("shoppingBag") as? List<Map<String, Any>>
                            ?: emptyList()
                    val productsCollection = firebaseFireStore.collection(COLLECTION_OF_PRODUCTS)
                    productsCollection.get()
                        .addOnSuccessListener { querySnapshot ->
                            val products = mutableListOf<ProductItemDomainModel>()
                            for (document in querySnapshot.documents) {
                                val id = document.getString("id")
                                val name = document.getString("name")
                                val description = document.getString("description")
                                val price = document.getDouble("price")?.toFloat()
                                val peopleRatedCounter = document.getLong("peopleRatedCounter") ?: 0
                                val rate = document.getLong("rate") ?: 0
                                val sexSerialized = document.getString("sex")
                                val productCategorySerialized =
                                    document.getString("productCategory")
                                val subCategory = document.get("subCategory")

                                val colorSizeInformationList =
                                    mutableListOf<ProductColorSizeInformationDomainModel>()
                                val colorSizeInformationArray =
                                    document.get("colorSizeInformation") as? List<HashMap<String, Any>>
                                colorSizeInformationArray?.forEach { colorSizeMap ->
                                    val colorName = colorSizeMap["colorName"] as? String
                                    val colorCode = colorSizeMap["colorCode"] as? String
                                    val photoUrl = colorSizeMap["photoUrl"] as? String

                                    val sizeRemoteModelList =
                                        mutableListOf<ProductSizeDomainModel>()
                                    val sizeRemoteModelArray =
                                        colorSizeMap["sizeUiModel"] as? List<HashMap<String, Any>>
                                    sizeRemoteModelArray?.forEach { sizeMap ->
                                        val size = try {
                                            (sizeMap["size"] as? Double)?.toFloat()
                                                ?: throw NumberFormatException()
                                        } catch (e: NumberFormatException) {
                                            try {
                                                (sizeMap["size"] as? Long)?.toFloat() ?: 0f
                                            } catch (e: Exception) {
                                                0f
                                            }
                                        }
                                        val availablePieces =
                                            sizeMap["availablePieces"] as? Int ?: 0
                                        sizeRemoteModelList.add(
                                            ProductSizeDomainModel(
                                                size,
                                                availablePieces
                                            )
                                        )
                                    }

                                    colorSizeInformationList.add(
                                        ProductColorSizeInformationDomainModel(
                                            colorName,
                                            colorCode,
                                            photoUrl,
                                            sizeRemoteModelList
                                        )
                                    )
                                }

                                val sex = Sex.fromSerializedForm(sexSerialized ?: "")
                                val productCategory =
                                    ProductCategory.fromSerializedForm(
                                        productCategorySerialized ?: ""
                                    )
                                val product = ProductItemDomainModel(
                                    id = id,
                                    name = name,
                                    description = description,
                                    price = price,
                                    isSaved = savedItems.contains(id),
                                    isInShoppingBag = shoppingBagList.any { item -> item["productId"] == id },
                                    peopleRatedCounter = peopleRatedCounter,
                                    rate = rate,
                                    sex = sex,
                                    productCategory = productCategory,
                                    subCategory = subCategory ?: "",
                                    colorSizeInformation = colorSizeInformationList
                                )
                                products.add(product)
                            }
                            continuation.resume(products)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun getProduct(userId: String, productId: String): ProductItemDomainModel =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(userId)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.data
                    val savedItems = userData?.get("savedProducts") as? List<String> ?: emptyList()
                    val shoppingBagList =
                        documentSnapshot.get("shoppingBag") as? List<Map<String, Any>>
                            ?: emptyList()
                    val productsCollection = firebaseFireStore.collection(COLLECTION_OF_PRODUCTS)
                    val productDocument = productsCollection.document(productId)
                    productDocument.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val data = documentSnapshot.data
                            val id = data?.get("id") as? String
                            val name = data?.get("name") as? String
                            val description = data?.get("description") as? String
                            val price = (data?.get("price") as? Double)?.toFloat()
                            val peopleRatedCounter = (data?.get("peopleRatedCounter") as? Long) ?: 0
                            val rate = (data?.get("rate") as? Long) ?: 0
                            val sex = Sex.valueOf(data?.get("sex") as? String ?: "MEN")
                            val productCategory =
                                ProductCategory.valueOf(
                                    data?.get("productCategory") as? String ?: "SHOE"
                                )
                            val subCategory = data?.get("subCategory")

                            val colorSizeInformation =
                                mutableListOf<ProductColorSizeInformationDomainModel>()
                            val colorSizeInfoArray =
                                data?.get("colorSizeInformation") as? List<Map<String, Any>>
                            colorSizeInfoArray?.forEach { colorSizeInfoMap ->
                                val colorName = colorSizeInfoMap["colorName"] as? String
                                val colorCode = colorSizeInfoMap["colorCode"] as? String
                                val photoUrl = colorSizeInfoMap["photoUrl"] as? String

                                val sizeDomainModel = mutableListOf<ProductSizeDomainModel>()
                                val sizeUiModelArray =
                                    colorSizeInfoMap["sizeUiModel"] as? List<Map<String, Any>>
                                sizeUiModelArray?.forEach { sizeUiModelMap ->
                                    val size = try {
                                        (sizeUiModelMap["size"] as? Double)?.toFloat()
                                            ?: throw NumberFormatException()
                                    } catch (e: NumberFormatException) {
                                        try {
                                            (sizeUiModelMap["size"] as? Long)?.toFloat() ?: 0f
                                        } catch (e: Exception) {
                                            0f
                                        }
                                    }
                                    val availablePieces =
                                        (sizeUiModelMap["availablePieces"] as? Long)?.toInt() ?: 0
                                    sizeDomainModel.add(
                                        ProductSizeDomainModel(
                                            size,
                                            availablePieces
                                        )
                                    )
                                }

                                colorSizeInformation.add(
                                    ProductColorSizeInformationDomainModel(
                                        colorName,
                                        colorCode,
                                        photoUrl,
                                        sizeDomainModel
                                    )
                                )
                            }

                            val product = ProductItemDomainModel(
                                id = id,
                                name = name,
                                description = description,
                                price = price,
                                isSaved = savedItems.contains(id),
                                isInShoppingBag = shoppingBagList.any { item -> item["productId"] == id },
                                peopleRatedCounter = peopleRatedCounter,
                                rate = rate,
                                sex = sex,
                                productCategory = productCategory,
                                subCategory = subCategory ?: "",
                                colorSizeInformation = colorSizeInformation
                            )
                            continuation.resume(product)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun saveProduct(userId: String, productId: String): Unit =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(userId)
            userDocument.update("savedProducts", FieldValue.arrayUnion(productId))
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun unSaveProduct(userId: String, productId: String): Unit =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(userId)
            userDocument.update("savedProducts", FieldValue.arrayRemove(productId))
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun getSavedItems(userId: String): List<ProductItemDomainModel> =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(userId)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.data
                    val savedItems = userData?.get("savedProducts") as? List<String> ?: emptyList()
                    if (savedItems.isEmpty()) {
                        continuation.resume(emptyList())
                        return@addOnSuccessListener
                    }
                    val productsCollection = firebaseFireStore.collection(COLLECTION_OF_PRODUCTS)
                    productsCollection.whereIn("id", savedItems).get()
                        .addOnSuccessListener { querySnapshot ->
                            val products = mutableListOf<ProductItemDomainModel>()
                            for (document in querySnapshot.documents) {
                                val id = document.getString("id")
                                val name = document.getString("name")
                                val description = document.getString("description")
                                val price = document.getDouble("price")?.toFloat()
                                val peopleRatedCounter = document.getLong("peopleRatedCounter") ?: 0
                                val rate = document.getLong("rate") ?: 0
                                val sexSerialized = document.getString("sex")
                                val productCategorySerialized =
                                    document.getString("productCategory")
                                val subCategory = document.get("subCategory")

                                val colorSizeInformationList =
                                    mutableListOf<ProductColorSizeInformationDomainModel>()
                                val colorSizeInformationArray =
                                    document.get("colorSizeInformation") as? List<HashMap<String, Any>>
                                colorSizeInformationArray?.forEach { colorSizeMap ->
                                    val colorName = colorSizeMap["colorName"] as? String
                                    val colorCode = colorSizeMap["colorCode"] as? String
                                    val photoUrl = colorSizeMap["photoUrl"] as? String

                                    val sizeRemoteModelList =
                                        mutableListOf<ProductSizeDomainModel>()
                                    val sizeRemoteModelArray =
                                        colorSizeMap["sizeUiModel"] as? List<HashMap<String, Any>>
                                    sizeRemoteModelArray?.forEach { sizeMap ->
                                        val size = sizeMap["size"] as? Float
                                        val availablePieces =
                                            sizeMap["availablePieces"] as? Int ?: 0
                                        sizeRemoteModelList.add(
                                            ProductSizeDomainModel(
                                                size,
                                                availablePieces
                                            )
                                        )
                                    }

                                    colorSizeInformationList.add(
                                        ProductColorSizeInformationDomainModel(
                                            colorName,
                                            colorCode,
                                            photoUrl,
                                            sizeRemoteModelList
                                        )
                                    )
                                }

                                val sex = Sex.fromSerializedForm(sexSerialized ?: "")
                                val productCategory =
                                    ProductCategory.fromSerializedForm(
                                        productCategorySerialized ?: ""
                                    )

                                val product = ProductItemDomainModel(
                                    id = id,
                                    name = name,
                                    description = description,
                                    price = price,
                                    isSaved = savedItems.contains(id),
                                    peopleRatedCounter = peopleRatedCounter,
                                    rate = rate,
                                    sex = sex,
                                    productCategory = productCategory,
                                    subCategory = subCategory ?: "",
                                    colorSizeInformation = colorSizeInformationList
                                )
                                products.add(product)
                            }
                            continuation.resume(products)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun placeAnOrder(orderDomainModel: OrderDomainModel): Unit =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(orderDomainModel.userId!!)
            val newOrderRef = firebaseFireStore.collection(COLLECTION_OF_ORDERS).document()
            newOrderRef
                .set(orderDomainModel.toOrderRemoteModel())
                .addOnSuccessListener {
                    val documentId = newOrderRef.id
                    val updatedOrder = orderDomainModel.copy(id = documentId).toOrderRemoteModel()
                    newOrderRef.set(updatedOrder, SetOptions.merge())
                    userDocument.update("shoppingBag", FieldValue.delete())
                        .addOnSuccessListener {
                            continuation.resume(Unit)
                        }.addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun addProductToShoppingBag(
        userId: String,
        orderItemDomainModel: OrderItemDomainModel
    ): Unit = suspendCoroutine { continuation ->
        val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
        val userDocument = usersCollection.document(userId)
        userDocument.update(
            "shoppingBag",
            FieldValue.arrayUnion(orderItemDomainModel.toOrderItemRemoteModel())
        )
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    override suspend fun removeProductFromShoppingBag(
        userId: String,
        orderItemDomainModel: OrderItemDomainModel
    ): Unit = suspendCoroutine { continuation ->
        val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
        val userDocument = usersCollection.document(userId)
        userDocument.update(
            "shoppingBag",
            FieldValue.arrayRemove(orderItemDomainModel.toOrderItemRemoteModel())
        )
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    override suspend fun getProductsFromShoppingBag(userId: String): List<OrderItemDomainModel> =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(userId)

            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val shoppingBagList =
                            documentSnapshot.get("shoppingBag") as? List<Map<String, Any>>
                                ?: emptyList()
                        if (shoppingBagList.isEmpty()) {
                            continuation.resume(emptyList())
                            return@addOnSuccessListener
                        }

                        val orderItems = shoppingBagList.map { itemMap ->
                            val productId = itemMap["productId"] as? String
                            val name = itemMap["name"] as? String
                            val price = (itemMap["price"] as? Double)?.toFloat()
                            val photoUrl = itemMap["photoUrl"] as? String
                            val colorCode = itemMap["colorCode"] as? String
                            val quantity = (itemMap["quantity"] as? Long)?.toInt() ?: 1
                            val colorName = itemMap["colorName"] as? String
                            val size = (itemMap["size"] as? Long)?.toInt()
                            val sexValue = itemMap["sex"] as? String
                            val sex = sexValue?.let { Sex.fromSerializedForm(it) }
                            OrderItemDomainModel(
                                productId, name, price, photoUrl, colorCode, quantity, colorName,
                                size, sex
                            )
                        }
                        continuation.resume(orderItems)
                    } else {
                        continuation.resume(emptyList())
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun logout() = firebaseAuth.signOut()

    override suspend fun updateUserInFireStore(appUserDomainModel: AppUserDomainModel): Unit =
        suspendCoroutine { continuation ->
            val usersCollection = firebaseFireStore.collection(COLLECTION_OF_USERS)
            val userDocument = usersCollection.document(appUserDomainModel.id!!)
            val updatesMap = mutableMapOf<String, Any>()
            appUserDomainModel.name?.let { updatesMap["name"] = it }
            appUserDomainModel.password?.let { updatesMap["password"] = it }
            appUserDomainModel.email?.let { updatesMap["email"] = it }
            appUserDomainModel.phone?.let { updatesMap["phone"] = it }
            userDocument.update(updatesMap)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
}
