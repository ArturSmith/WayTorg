package com.way_torg.myapplication.presentation.order

import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.OrderStatus
import com.way_torg.myapplication.domain.entity.ProductWrapper
import kotlinx.coroutines.flow.StateFlow

interface OrdersComponent {
    val model: StateFlow<OrdersStore.State>


    fun onClickBack()
    fun onClickTab(status: OrderStatus)
    fun onClickOrder(order: Order)
    fun closeModalSheet()
    fun onClickIncreaseQuantity(order: Order, product: ProductWrapper)
    fun onClickDecreaseQuantity(order: Order, product: ProductWrapper)
    fun onClickDeleteProduct(order: Order, product: ProductWrapper)
    fun onClickStatus(order: Order, status: OrderStatus)
    fun deleteOrder(order: Order)

}


