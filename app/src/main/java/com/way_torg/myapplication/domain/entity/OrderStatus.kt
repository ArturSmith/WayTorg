package com.way_torg.myapplication.domain.entity

enum class OrderStatus(val index:Int){
    UNPAID(0), PAID(1), CANCELED(2), DELAYED(3)
}