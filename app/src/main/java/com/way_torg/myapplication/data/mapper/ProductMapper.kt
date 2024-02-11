package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.dto.ProductDto
import com.way_torg.myapplication.domain.entity.Product

fun Product.toDto() = ProductDto(id, name, category, description, count, price, discount, pictures, rating)

fun ProductDto.toEntity() =
    Product(id, name, category, description, count, price, discount, pictures, rating)