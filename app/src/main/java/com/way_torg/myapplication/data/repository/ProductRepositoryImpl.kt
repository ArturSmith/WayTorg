package com.way_torg.myapplication.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.way_torg.myapplication.data.local.db.AppDao
import com.way_torg.myapplication.data.mapper.toDto
import com.way_torg.myapplication.data.mapper.toEntity
import com.way_torg.myapplication.data.mapper.toModel
import com.way_torg.myapplication.data.network.dto.ProductDto
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.ProductRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val appDao: AppDao
) : ProductRepository {
    private val ref = storage.reference
    override suspend fun createProduct(product: Product, uris: List<Uri>): Result<Boolean> {
        return try {
            if (uris.isNotEmpty()) {
                val paths = mutableListOf<String>()
                uris.forEach { uri ->
                    val productRef = ref.child("products images/${UUID.randomUUID()}")
                    productRef.putFile(uri)
                        .continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            productRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result?.let {
                                    paths.add(it.toString())
                                }
                            }
                        }.await()
                }
                val newProduct = product.copy(pictures = paths).toDto()
                firestore.collection(PRODUCTS).document().set(newProduct).await()
            } else {
                firestore.collection(PRODUCTS).document().set(product.toDto()).await()
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProduct(id: String): Result<Boolean> {
        return try {
            firestore.collection(PRODUCTS).document(id).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editProduct(product: Product): Result<Boolean> {
        return try {
            firestore.collection(PRODUCTS).document(product.id)
                .set(product, SetOptions.merge()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addProductToBasket(product: Product): Result<Boolean> {
        return try {
            appDao.addProduct(product.toModel())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProductFromBasket(id: String): Result<Boolean> {
        return try {
            appDao.deleteProduct(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllProducts(): Flow<List<Product>> = callbackFlow {
        val observer = firestore.collection(PRODUCTS).addSnapshotListener { value, error ->
            if (error != null || value?.documents?.isEmpty()==true) return@addSnapshotListener

            val products = value?.documents?.map {
                it.toObject<ProductDto>()?.let { it.toEntity() } ?: Product.defaultInstance()
            } ?: emptyList()
            trySend(products)
        }

        awaitClose {
            observer.remove()
            this.cancel()
        }
    }

    override suspend fun getProductsFromBasket() =
        appDao.getProducts().map {
            it.id
        }

    override fun getCountOfProductsFromBasket() = appDao.getCountOfProducts()


    private companion object {
        const val PRODUCTS = "products"
    }
}