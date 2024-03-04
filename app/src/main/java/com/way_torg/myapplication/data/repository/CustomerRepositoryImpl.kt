package com.way_torg.myapplication.data.repository

import com.way_torg.myapplication.data.local.db.AppDao
import com.way_torg.myapplication.data.mapper.toEntity
import com.way_torg.myapplication.data.mapper.toModel
import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : CustomerRepository {
    override suspend fun saveCustomerIntoDb(customerInfo: CustomerInfo) =
        try {
            appDao.addCustomer(customerInfo.toModel())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getCustomersFromDb() = appDao.getCustomers().map { it.toEntity() }


}