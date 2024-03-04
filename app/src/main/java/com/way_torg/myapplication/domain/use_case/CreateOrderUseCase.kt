package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) = repository.createdOrder(order)
}