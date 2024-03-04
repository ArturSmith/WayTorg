package com.way_torg.myapplication.domain.entity

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val id: String,
    val products: List<ProductWrapper>,
    val customerInfo: CustomerInfo,
    val orderDate: Long,
    val status: OrderStatus = OrderStatus.UNPAID,
) : Parcelable {
    fun totalQuantityOfProducts() = products.map { it.quantity }.sum()
    fun totalPriceWithDiscount() = products.map { it.getTotalPriceWithDiscount() }.sum()
    fun totalPriceWithoutDiscount() = products.map { it.getTotalPriceWithoutDiscount() }.sum()
    fun totalDiscount() = products.map { it.getDiscount() }.sum()
    fun averageDiscountInPercent() = this.totalDiscount() * 100 / totalPriceWithoutDiscount()

    fun increaseQuantityOfProduct(productId: String): Order {
        val newList =
            products.map { if (it.product.id == productId) it.increaseQuantity() else it }

        return this.copy(products = newList)
    }

    fun decreaseQuantityOfProduct(productId: String): Order {
        val newList =
            products.map { if (it.product.id == productId) it.decreaseQuantity() else it }
        return this.copy(products = newList)
    }

    fun changeStatus(status: OrderStatus) = this.copy(status = status)


    fun deleteProduct(productWrapper: ProductWrapper): Order {
        val newList = products.toMutableList()
        newList.remove(productWrapper)
        return this.copy(products = newList.toList())
    }


    companion object {
        val defaultInstance = Order(
            "",
            emptyList(),
            CustomerInfo.defaultInstance,
            0,
            status = OrderStatus.UNPAID
        )
    }
}

