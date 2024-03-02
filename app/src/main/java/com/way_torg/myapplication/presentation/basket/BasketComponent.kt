package com.way_torg.myapplication.presentation.basket

import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface BasketComponent {
    val model: StateFlow<BasketStore.State>

    fun increaseQuantity(productId:String)
    fun decreaseQuantity(productId: String)
    fun onClickProduct(product: Product)
    fun delete(productId: String)
    fun setCustomerName(name:String)
    fun setCustomerAddress(address:String)
    fun setCustomerContact(contact:String)
    fun setCustomerMessage(message:String)
    fun onClickCustomer(customerInfo: CustomerInfo)
    fun order()
    fun hideSheet()
    fun showSheet()
    fun onClickBack()

}