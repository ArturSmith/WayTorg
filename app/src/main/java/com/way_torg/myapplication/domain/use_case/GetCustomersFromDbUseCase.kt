package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository

class GetCustomersFromDbUseCase(private val repository: OrderRepository) {
     operator fun invoke() = repository.getCustomersFromDb()
}