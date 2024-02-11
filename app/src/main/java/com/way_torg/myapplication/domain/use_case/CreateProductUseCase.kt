package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.ProductRepository

data class CreateProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product) = repository.createProduct(product)
}
