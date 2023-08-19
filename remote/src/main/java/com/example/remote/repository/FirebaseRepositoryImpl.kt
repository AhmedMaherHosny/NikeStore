package com.example.remote.repository

import com.example.core.Constants.COLLECTION_OF_PRODUCTS
import com.example.core.Constants.COLLECTION_OF_USERS
import com.example.core.enums.ProductCategory
import com.example.core.enums.Sex
import com.example.domain.models.AppUserDomainModel
import com.example.domain.models.ProductColorSizeInformationDomainModel
import com.example.domain.models.ProductItemDomainModel
import com.example.domain.models.ProductSizeDomainModel
import com.example.domain.repository.remote.FirebaseRepository
import com.example.remote.mappers.toAppUserDomain
import com.example.remote.mappers.toAppUserDomainModel
import com.example.remote.mappers.toAppUserRemoteModel
import com.example.remote.models.AppUserRemoteModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun getProducts(): List<ProductItemDomainModel> =
        suspendCoroutine { continuation ->
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
                        val productCategorySerialized = document.getString("productCategory")
                        val subCategory = document.get("subCategory")

                        val colorSizeInformationList =
                            mutableListOf<ProductColorSizeInformationDomainModel>()
                        val colorSizeInformationArray =
                            document.get("colorSizeInformation") as? List<HashMap<String, Any>>
                        colorSizeInformationArray?.forEach { colorSizeMap ->
                            val colorName = colorSizeMap["colorName"] as? String
                            val colorCode = colorSizeMap["colorCode"] as? String
                            val photoUrl = colorSizeMap["photoUrl"] as? String

                            val sizeRemoteModelList = mutableListOf<ProductSizeDomainModel>()
                            val sizeRemoteModelArray =
                                colorSizeMap["sizeUiModel"] as? List<HashMap<String, Any>>
                            sizeRemoteModelArray?.forEach { sizeMap ->
                                val size = sizeMap["size"] as? Float
                                val availablePieces = sizeMap["availablePieces"] as? Int ?: 0
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
                            ProductCategory.fromSerializedForm(productCategorySerialized ?: "")

                        val product = ProductItemDomainModel(
                            id = id,
                            name = name,
                            description = description,
                            price = price,
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

    override suspend fun getProduct(id: String): ProductItemDomainModel =
        suspendCoroutine { continuation ->
            val productsCollection = firebaseFireStore.collection(COLLECTION_OF_PRODUCTS)
            val productDocument = productsCollection.document(id)
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
                        ProductCategory.valueOf(data?.get("productCategory") as? String ?: "SHOE")
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
                                (sizeUiModelMap["size"] as? Double)?.toFloat() ?: throw NumberFormatException()
                            } catch (e: NumberFormatException) {
                                try {
                                    (sizeUiModelMap["size"] as? Long)?.toFloat() ?: 0f
                                } catch (e: Exception) {
                                    0f
                                }
                            }
                            val availablePieces = (sizeUiModelMap["availablePieces"] as? Long)?.toInt() ?: 0
                            sizeDomainModel.add(ProductSizeDomainModel(size, availablePieces))
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
}
