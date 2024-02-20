package com.way_torg.myapplication.domain.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: Category,
    val description: String,
    val count: Int,
    val price: Double,
    val discount: Double,
    val pictures: List<String>,
    val rating: Double
) :Parcelable{

    companion object {
        fun defaultInstance() = Product(
            "",
            "",
            Category("", ""),
            "",
            0,
            0.0,
            0.0,
            emptyList(),
            0.0)
    }

}
