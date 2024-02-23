package com.way_torg.myapplication.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductWrapper(
    val product: Product,
    val quantityInBasket: Int,
    val totalPrice: Double
):Parcelable {
    fun getTotalPriceWithDiscount(): Double {
        val priceWithoutDiscount = quantityInBasket * product.price
        val discount = priceWithoutDiscount * product.discount / 100
        return priceWithoutDiscount - discount
    }

    fun getTotalPriceWithoutDiscount() = quantityInBasket * product.price

    fun getDiscount(): Double {
        val priceWithoutDiscount = quantityInBasket * product.price
        return priceWithoutDiscount * product.discount / 100
    }

    fun increaseQuantity() =
        if (quantityInBasket < product.count)
            this.copy(quantityInBasket = quantityInBasket + 1)
        else this

    fun decreaseQuantity() =
        if (quantityInBasket > 1)
            this.copy(quantityInBasket = quantityInBasket - 1)
        else this
}