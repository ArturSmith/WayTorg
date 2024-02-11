package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.Product

interface ProductRepository {
    suspend fun createProduct(product: Product) : Result<Boolean>
}