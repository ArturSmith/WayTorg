package com.way_torg.myapplication.domain.entity


data class ProductStringHandler(private val productWrapper: ProductWrapper) {
    val strTotalPriceWithoutDiscount
        get() = String.format("%.2f", productWrapper.getTotalPriceWithoutDiscount())
    val strTotalDiscount
        get() = String.format("%.2f", productWrapper.getDiscount())

    val strTotalPrice
        get() = String.format("%.2f", productWrapper.getTotalPriceWithDiscount())

}
