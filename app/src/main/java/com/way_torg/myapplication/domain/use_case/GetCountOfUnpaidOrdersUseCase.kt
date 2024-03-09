package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository
import javax.inject.Inject

class GetCountOfUnpaidOrdersUseCase @Inject constructor(private val repository: OrderRepository) {
    operator fun invoke() = repository.getCountOfUnpaidOrders()
}