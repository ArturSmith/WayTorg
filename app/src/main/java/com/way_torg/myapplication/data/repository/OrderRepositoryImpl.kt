package com.way_torg.myapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.way_torg.myapplication.data.local.db.AppDao
import com.way_torg.myapplication.data.mapper.toDto
import com.way_torg.myapplication.data.mapper.toEntity
import com.way_torg.myapplication.data.mapper.toModel
import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appDao: AppDao
) : OrderRepository {
    override suspend fun order(order: Order) =
        try {
            firestore.collection(ORDERS).document().set(order.toDto()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun saveCustomerIntoDb(customerInfo: CustomerInfo) =
        try {
            appDao.addCustomer(customerInfo.toModel())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getCustomersFromDb() = appDao.getCustomers().map { it.toEntity() }

    private companion object {
        const val ORDERS = "orders"
    }
}