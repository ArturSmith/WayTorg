package com.way_torg.myapplication.domain.entity

data class OrderStringHandler(private val order: Order) {
    val strTotalPriceWithDiscount
        get() = String.format("%.2f", order.totalPriceWithDiscount())
    val strTotalDiscount
        get() = String.format("%.2f", order.totalDiscount())

}