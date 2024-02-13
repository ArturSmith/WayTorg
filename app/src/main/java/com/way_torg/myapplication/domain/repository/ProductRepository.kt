package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun createProduct(product: Product) : Result<Boolean>
    suspend fun addToBasket(product: Product):Result<Boolean>
    fun getAllProducts(): Flow<List<Product>>

    fun getProductsFromBasket(): Flow<List<Product>>
}