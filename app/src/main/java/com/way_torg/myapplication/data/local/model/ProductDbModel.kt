package com.way_torg.myapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.way_torg.myapplication.domain.entity.Category

@Entity(tableName = "products")
data class ProductDbModel(
    @PrimaryKey val id: String
)
