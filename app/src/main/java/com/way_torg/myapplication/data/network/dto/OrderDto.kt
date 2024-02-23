package com.way_torg.myapplication.data.network.dto


class OrderDto() {
    var products: List<ProductWrapperDto> = emptyList()
    var customerInfo: CustomerInfoDto = CustomerInfoDto()
    var orderDate: Long = 0

    constructor(
        products: List<ProductWrapperDto>,
        customerInfoDto: CustomerInfoDto,
        orderDate: Long
    ) : this() {
        this.products = products
        this.customerInfo = customerInfoDto
        this.orderDate = orderDate
    }
}