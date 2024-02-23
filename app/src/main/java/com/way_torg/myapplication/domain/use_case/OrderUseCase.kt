package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository

class OrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) = repository.order(order)
}