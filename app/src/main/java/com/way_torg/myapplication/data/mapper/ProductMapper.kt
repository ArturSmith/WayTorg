package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.local.model.ProductDbModel
import com.way_torg.myapplication.data.network.dto.ProductDto
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product

fun Product.toModel() = ProductDbModel(id)

fun Product.toDto() = ProductDto(id, name, category.toDto(), description, count, price, discount, pictures, rating)
fun ProductDto.toEntity() = Product(id, name, category.toEntity(), description, count, price, discount, pictures, rating)