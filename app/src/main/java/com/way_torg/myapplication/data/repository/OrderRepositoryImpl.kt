package com.way_torg.myapplication.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.way_torg.myapplication.data.local.db.AppDao
import com.way_torg.myapplication.data.mapper.toDto
import com.way_torg.myapplication.data.mapper.toEntity
import com.way_torg.myapplication.data.mapper.toModel
import com.way_torg.myapplication.data.network.dto.OrderDto
import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.OrderStatus
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.domain.repository.OrderRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {
    override suspend fun createdOrder(order: Order) =
        try {
            firestore.collection(ORDERS).document(order.id).set(order.toDto()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun editOrder(order: Order) =
        try {
            firestore.collection(ORDERS).document(order.id).set(order, SetOptions.merge()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun deleteOrder(orderId: String) =
        try {
            firestore.collection(ORDERS).document(orderId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getOrders() = callbackFlow {
        val observer = firestore.collection(ORDERS).addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener

            val orders = value?.documents?.map {
                it.toObject<OrderDto>()?.toEntity() ?: Order.defaultInstance
            } ?: emptyList()
            trySend(orders)
        }
        awaitClose {
            observer.remove()
            this.cancel()
        }
    }

    private companion object {
        const val ORDERS = "orders"
    }
}