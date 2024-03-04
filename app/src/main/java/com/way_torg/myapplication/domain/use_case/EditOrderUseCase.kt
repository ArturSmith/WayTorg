package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository
import javax.inject.Inject

class EditOrderUseCase @Inject constructor(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) = repository.editOrder(order)
}