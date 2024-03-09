package com.way_torg.myapplication.domain.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val serialNumber: Int = 0,
    val category: Category,
    val description: String,
    val count: Int,
    val price: Double,
    val discount: Double,
    val pictures: Map<String,String>,
    val rating: Double
) : Parcelable {

    companion object {
        fun defaultInstance() = Product(
            "",
            "",
            serialNumber = 0,
            Category("", ""),
            "",
            0,
            0.0,
            0.0,
            emptyMap(),
            0.0
        )

        const val ID = "id"
    }

}
