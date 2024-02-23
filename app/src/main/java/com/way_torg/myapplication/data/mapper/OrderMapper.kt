package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.network.dto.OrderDto
import com.way_torg.myapplication.domain.entity.Order


fun Order.toDto() = OrderDto(products.toDto(), customerInfo.toDto(), orderDate)
fun OrderDto.toEntity() = Order(products.toEntity(), customerInfo.toEntity(), orderDate)