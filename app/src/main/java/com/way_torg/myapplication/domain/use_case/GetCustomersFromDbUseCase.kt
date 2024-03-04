package com.way_torg.myapplication.domain.use_case

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.CustomerRepository
import com.way_torg.myapplication.domain.repository.OrderRepository
import javax.inject.Inject

class GetCustomersFromDbUseCase @Inject constructor(private val repository: CustomerRepository) {
     operator fun invoke() = repository.getCustomersFromDb()
}