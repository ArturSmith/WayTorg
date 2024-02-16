package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.local.model.CategoryDbModel
import com.way_torg.myapplication.data.network.dto.CategoryDto
import com.way_torg.myapplication.domain.entity.Category

fun Category.toModel() = CategoryDbModel(id, name)

fun CategoryDbModel.toEntity() = Category(id, name)
fun List<CategoryDbModel>.toEntity() = map { it.toEntity() }

fun Category.toDto() = CategoryDto(id, name)
fun CategoryDto.toEntity() = Category(id, name)