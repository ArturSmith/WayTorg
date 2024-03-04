package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.CustomerInfo
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun saveCustomerIntoDb(customerInfo: CustomerInfo): Result<Boolean>
    fun getCustomersFromDb(): Flow<List<CustomerInfo>>
}