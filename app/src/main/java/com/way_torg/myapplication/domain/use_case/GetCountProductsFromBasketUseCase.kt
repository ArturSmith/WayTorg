package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class GetCountProductsFromBasketUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke() = repository.getCountOfProductsFromBasket()
}