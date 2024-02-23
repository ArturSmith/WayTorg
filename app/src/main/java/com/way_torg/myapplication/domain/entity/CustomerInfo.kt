package com.way_torg.myapplication.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerInfo(
    val id:String,
    val name: String,
    val address: String,
    val contact: String,
    val message: String
):Parcelable {
    companion object {
        val defaultInstance = CustomerInfo("", "", "", "", "")
    }
}
