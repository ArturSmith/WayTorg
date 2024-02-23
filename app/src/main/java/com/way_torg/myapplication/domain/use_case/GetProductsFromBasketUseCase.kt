package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsFromBasketUseCase @Inject constructor(private val repository: ProductRepository) {
     operator fun invoke() = repository.getProductsFromBasket()
}