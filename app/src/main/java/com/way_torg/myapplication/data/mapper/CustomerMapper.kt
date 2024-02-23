package com.way_torg.myapplication.data.mapper

import com.way_torg.myapplication.data.local.model.CustomerInfoDbModel
import com.way_torg.myapplication.data.network.dto.CustomerInfoDto
import com.way_torg.myapplication.domain.entity.CustomerInfo

fun CustomerInfo.toModel() = CustomerInfoDbModel(id, name, address, contact, message)
fun CustomerInfoDbModel.toEntity() = CustomerInfo(id, name, address, contact, message)
fun List<CustomerInfoDbModel>.toEntity() = map { it.toEntity() }


fun CustomerInfo.toDto() = CustomerInfoDto(id, name, address, contact, message)
fun CustomerInfoDto.toEntity() = CustomerInfo(id, name, address, contact, message)
