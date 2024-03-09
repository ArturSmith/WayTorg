package com.way_torg.myapplication.data.network.dto


class ProductWrapperDto() {
    var product: ProductDto = ProductDto()
    var quantity: Int = 0

    constructor(
        productDto: ProductDto,
        quantity: Int,
    ) : this() {
        this.product = productDto
        this.quantity = quantity
    }
}