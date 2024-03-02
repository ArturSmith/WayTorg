package com.way_torg.myapplication.extensions

import com.way_torg.myapplication.presentation.basket.BasketStore


fun BasketStore.State.Initial.isAnyRequiredFieldEmpty() =
    customerInfo.name.isEmpty() || customerInfo.contact.isEmpty()

fun BasketStore.State.Initial.validateFields() = this.copy(
    isCustomerNameError = customerInfo.name.isEmpty(),
    isCustomerContactError = customerInfo.contact.isEmpty()
)
fun BasketStore.State.Initial.isOrderingEnable() = order.products.isNotEmpty()