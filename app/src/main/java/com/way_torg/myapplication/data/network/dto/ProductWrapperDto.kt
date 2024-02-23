package com.way_torg.myapplication.data.network.dto

import com.way_torg.myapplication.domain.entity.Product

class ProductWrapperDto() {
    var product: ProductDto = ProductDto()
    var quantityInBasket: Int = 0
    var totalPrice: Double = 0.0

    constructor(
        productDto: ProductDto,
        quantity: Int,
        totalPrice: Double
    ) : this() {
        this.product = productDto
        this.quantityInBasket = quantity
        this.totalPrice = totalPrice
    }
}