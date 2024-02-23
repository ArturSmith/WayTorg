package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository

class SaveCustomerIntoDbUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(customerInfo: CustomerInfo) =
        repository.saveCustomerIntoDb(customerInfo)
}