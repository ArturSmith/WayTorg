package com.way_torg.myapplication.data.dto

import com.way_torg.myapplication.domain.entity.Category

data class ProductDto(
    val id: String,
    val name: String,
    val category: Category,
    val description:String,
    val count:Int,
    val price:Double,
    val discount:Double,
    val pictures: List<String>,
    val rating:Double
)
