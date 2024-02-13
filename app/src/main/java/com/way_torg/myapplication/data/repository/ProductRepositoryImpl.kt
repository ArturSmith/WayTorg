package com.way_torg.myapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.way_torg.myapplication.data.mapper.toDto
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProductRepository {
    private val ref = storage.reference
    override suspend fun createProduct(product: Product): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun addToBasket(product: Product): Result<Boolean> {
        return Result.success(true)
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return flow {  }
    }

    override fun getProductsFromBasket(): Flow<List<Product>> {
        return flow {  }
    }

    companion object {
        private const val PRODUCTS = "products"
        private const val CATEGORIES = "categories"
    }
}