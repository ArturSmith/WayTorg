package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun order(order: Order): Result<Boolean>
    suspend fun saveCustomerIntoDb(customerInfo: CustomerInfo): Result<Boolean>
     fun getCustomersFromDb(): Flow<List<CustomerInfo>>

}