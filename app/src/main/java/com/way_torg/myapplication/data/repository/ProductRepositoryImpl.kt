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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appDao: AppDao,
    storage: FirebaseStorage
) : ProductRepository {
    private val ref = storage.reference
    override suspend fun createProduct(product: Product, uris: List<Uri>): Result<Boolean> {
        return try {
            if (uris.isNotEmpty()) {
                val paths = mutableMapOf<String,String>()
                uris.forEach { uri ->
                    val randomID = UUID.randomUUID().toString()
                    val productRef = ref.child("$PRODUCTS_IMAGES_REF/$randomID")
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
                                    paths.put(randomID, it.toString())
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

    override suspend fun deleteProduct(product: Product): Result<Boolean> {
        return try {
            product.pictures.forEach {
                val ref = ref.child("$PRODUCTS_IMAGES_REF/${it.key}")
                ref.delete().await()
            }
            firestore.collection(PRODUCTS).document(product.id).delete().await()
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

    override suspend fun cleanBasket() {
        appDao.cleanBasket()
    }

    override fun getAllProducts(): Flow<List<Product>> = callbackFlow {
        val observer = firestore.collection(PRODUCTS).addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener

            val products = value?.documents?.map {
                it.toObject<ProductDto>()?.toEntity() ?: Product.defaultInstance()
            } ?: emptyList()
            trySend(products)
        }

        awaitClose {
            observer.remove()
            this.cancel()
        }
    }


    override fun getProductsFromBasket() = appDao.getProducts()
        .map {
            it.map { it.id }
        }

    override suspend fun getProductsById(ids: List<String>) =
        if (ids.isNotEmpty()) {
            firestore.collection(PRODUCTS)
                .whereIn(Product.ID, ids)
                .get()
                .await()
                .documents
                .map {
                    it.toObject<ProductDto>()?.let { it.toEntity() }
                        ?: Product.defaultInstance()
                }
        } else emptyList()


    override fun getCountOfProductsFromBasket() = appDao.getCountOfProducts()


    private companion object {
        const val PRODUCTS = "products"
        const val PRODUCTS_IMAGES_REF = "products images"
    }
}