package com.way_torg.myapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.way_torg.myapplication.data.mapper.toDto
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.ProductRepository
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProductRepository {
    private val ref = storage.reference
    override suspend fun createProduct(product: Product): Result<Boolean> =
        try {
            // add pictures to storage

            // save product in db
            val result = firestore.collection(PRODUCTS).add(product.toDto()).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }



    companion object {
        private const val PRODUCTS = "products"
        private const val CATEGORIES = "categories"
    }
}