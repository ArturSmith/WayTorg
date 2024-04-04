package com.way_torg.myapplication.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
                val paths = savePictures(uris)
                val newProduct = product.copy(pictures = paths).toDto()
                firestore.collection(PRODUCTS).document(product.id).set(newProduct).await()
            } else {
                firestore.collection(PRODUCTS).document(product.id).set(product.toDto()).await()
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun savePictures(uris: List<Uri>): Map<String, String> {
        val paths = mutableMapOf<String, String>()
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
        return paths.toMap()
    }

    override suspend fun deleteProduct(product: Product): Result<Boolean> {
        // TODO("Need correct implementation of exception handling ")
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
        // TODO("Need correct implementation of exception handling ")
        return try {
            firestore.collection(PRODUCTS).document(product.id)
                .set(product, SetOptions.merge()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addProductToBasket(product: Product): Result<Boolean> {
        // TODO("Need correct implementation of exception handling ")
        return try {
            appDao.addProduct(product.toModel())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProductFromBasket(id: String): Result<Boolean> {
        // TODO("Need correct implementation of exception handling ")
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
            if (error != null) {
                return@addSnapshotListener
            }
            val products = value?.documents?.mapNotNull {
                it.toObject<ProductDto>()?.toEntity()
            } ?: emptyList()

            trySend(products)
        }
        awaitClose {
            observer.remove()
        }
    }

    override fun getProductsFromBasket(): Flow<List<String>> = appDao.getProducts()
        .map { productList -> productList.map { it.id } }
        .catch {
            emit(emptyList())
        }

    override suspend fun getProductsById(ids: List<String>): List<Product> {
        if (ids.isEmpty()) return emptyList()

        return try {
            val documents = firestore.collection(PRODUCTS)
                .whereIn(Product.ID, ids.take(10))
                .get()
                .await()
                .documents

            documents.mapNotNull { document -> document.toObject<ProductDto>()?.toEntity() }

        } catch (e: Exception) {
            emptyList()
        }
    }


    override fun getCountOfProductsFromBasket(): Flow<Int> = appDao.getCountOfProducts()
        .catch {
            emit(0)
        }


    private companion object {
        const val PRODUCTS = "products"
        const val PRODUCTS_IMAGES_REF = "products images"
    }
}