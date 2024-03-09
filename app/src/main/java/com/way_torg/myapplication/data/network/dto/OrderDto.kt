package com.way_torg.myapplication.data.network.dto

import com.way_torg.myapplication.domain.entity.OrderStatus


class OrderDto() {
    var id: String = ""
    var products: List<ProductWrapperDto> = emptyList()
    var customerInfo: CustomerInfoDto = CustomerInfoDto()
    var orderDate: Long = 0
    var status: OrderStatus = OrderStatus.UNPAID

    constructor(
        id: String,
        products: List<ProductWrapperDto>,
        customerInfoDto: CustomerInfoDto,
        orderDate: Long,
        status: OrderStatus
    ) : this() {
        this.id = id
        this.products = products
        this.customerInfo = customerInfoDto
        this.orderDate = orderDate
        this.status = status
    }

    companion object{
        const val STATUS = "status"
    }
}