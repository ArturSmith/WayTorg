package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.network.dto.OrderDto
import com.way_torg.myapplication.domain.entity.Order


fun Order.toDto() = OrderDto(
    id,
    products.toDto(),
    customerInfo.toDto(),
    orderDate,
    status
)

fun OrderDto.toEntity() = Order(
    id,
    products.toEntity(),
    customerInfo.toEntity(),
    orderDate,
    status
)