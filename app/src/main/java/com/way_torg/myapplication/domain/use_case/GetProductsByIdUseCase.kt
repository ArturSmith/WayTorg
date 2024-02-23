package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsByIdUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(ids: List<String>) = repository.getProductsById(ids)
}