package com.way_torg.myapplication.presentation.basket

import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface BasketComponent {
    val model: StateFlow<BasketStore.State>

    fun increaseQuantity(productId:String)
    fun decreaseQuantity(productId: String)
    fun onClickProduct(product: Product)
    fun delete(productId: String)
    fun order()
    fun hideSheet()
    fun showSheet()
    fun onClickBack()

}