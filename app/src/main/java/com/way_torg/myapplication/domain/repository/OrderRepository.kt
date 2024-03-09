package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.OrderStatus
import com.way_torg.myapplication.domain.entity.ProductWrapper
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createdOrder(order: Order): Result<Boolean>

    suspend fun editOrder(order: Order): Result<Boolean>
    suspend fun deleteOrder(orderId: String): Result<Boolean>
    fun getOrders(): Flow<List<Order>>

    fun getCountOfUnpaidOrders(): Flow<Int>

}