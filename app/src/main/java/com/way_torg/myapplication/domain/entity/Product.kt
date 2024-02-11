package com.way_torg.myapplication.domain.entity

data class Product(
    val id: String,
    val name: String,
    val category: Category,
    val description:String,
    val price:Double,
    val discount:Double,
    val pictures: List<String>,
    val rating:Double
)
