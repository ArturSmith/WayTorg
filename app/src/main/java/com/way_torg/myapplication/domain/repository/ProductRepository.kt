package com.way_torg.myapplication.domain.repository

import android.net.Uri
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductsById(ids: List<String>): List<Product>
    suspend fun createProduct(product: Product, uris: List<Uri>): Result<Boolean>
    suspend fun deleteProduct(product: Product): Result<Boolean>
    suspend fun editProduct(product: Product): Result<Boolean>
    suspend fun addProductToBasket(product: Product): Result<Boolean>
    suspend fun deleteProductFromBasket(id: String): Result<Boolean>
    suspend fun cleanBasket()
    fun getProductsFromBasket(): Flow<List<String>>
    fun getCountOfProductsFromBasket(): Flow<Int>
}