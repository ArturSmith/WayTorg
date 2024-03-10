package com.way_torg.myapplication.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
data class ProductWrapper(
    val product: Product,
    val quantity: Int
) : Parcelable {
    fun getTotalPriceWithoutDiscount() = quantity * product.price



    fun getDiscount(): Double {
        return getTotalPriceWithoutDiscount() * product.discount / 100
    }



    fun getTotalPriceWithDiscount(): Double {
        return getTotalPriceWithoutDiscount() - getDiscount()
    }



    fun increaseQuantity() =
        if (quantity < product.count)
            this.copy(quantity = quantity + 1)
        else this

    fun decreaseQuantity() =
        if (quantity > 1)
            this.copy(quantity = quantity - 1)
        else this


}