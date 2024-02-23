package com.way_torg.myapplication.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val products: List<ProductWrapper>,
    val customerInfo: CustomerInfo,
    val orderDate: Long
):Parcelable
