package com.way_torg.myapplication.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Category(
    val id: String,
    val name: String
):Parcelable {
    companion object {
        val defaultInstance = Category("Default","Default")
    }
}
