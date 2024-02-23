package com.way_torg.myapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("customers")
data class CustomerInfoDbModel(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val contact: String,
    val message: String
)
