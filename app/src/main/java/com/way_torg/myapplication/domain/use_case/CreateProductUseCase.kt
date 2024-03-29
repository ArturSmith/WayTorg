package com.way_torg.myapplication.domain.use_case

import android.net.Uri
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

data class CreateProductUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product, uris:List<Uri>) = repository.createProduct(product, uris)
}
