package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.local.model.ProductDbModel
import com.way_torg.myapplication.data.network.dto.ProductDto
import com.way_torg.myapplication.data.network.dto.ProductWrapperDto
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.entity.ProductWrapper

fun Product.toModel() = ProductDbModel(id)

fun Product.toDto() =
    ProductDto(id, name, serialNumber, category.toDto(), description, count, price, discount, pictures, rating)

fun ProductDto.toEntity() =
    Product(id, name, serialNumber, category.toEntity(), description, count, price, discount, pictures, rating)

fun ProductWrapper.toDto() = ProductWrapperDto(product.toDto(), quantity)
fun ProductWrapperDto.toEntity() = ProductWrapper(product.toEntity(), quantity)

fun List<ProductWrapper>.toDto() = map { it.toDto() }
fun List<ProductWrapperDto>.toEntity() = map { it.toEntity() }