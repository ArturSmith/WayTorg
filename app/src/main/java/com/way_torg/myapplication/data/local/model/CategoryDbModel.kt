package com.way_torg.myapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("categories")
data class CategoryDbModel(
    @PrimaryKey val id: String,
    val name: String
)
