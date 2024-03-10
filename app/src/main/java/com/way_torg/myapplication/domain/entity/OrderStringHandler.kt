package com.way_torg.myapplication.domain.entity

data class OrderStringHandler(private val order: Order) {
    val strTotalPriceWithDiscount
        get() = String.format("%.2", order.totalPriceWithDiscount())
    val strTotalDiscount
        get() = String.format("%.2", order.totalDiscount())

}