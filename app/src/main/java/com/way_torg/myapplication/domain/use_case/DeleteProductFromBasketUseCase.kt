package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

data class DeleteProductFromBasketUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(id:String) = repository.deleteProductFromBasket(id)
}
