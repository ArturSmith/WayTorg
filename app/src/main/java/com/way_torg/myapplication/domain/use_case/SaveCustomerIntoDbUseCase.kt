package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.CustomerRepository
import com.way_torg.myapplication.domain.repository.OrderRepository
import javax.inject.Inject

class SaveCustomerIntoDbUseCase @Inject constructor(private val repository: CustomerRepository) {
    suspend operator fun invoke(customerInfo: CustomerInfo) =
        repository.saveCustomerIntoDb(customerInfo)
}