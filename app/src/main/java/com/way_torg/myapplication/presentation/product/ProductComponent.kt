package com.way_torg.myapplication.presentation.product

import kotlinx.coroutines.flow.StateFlow

interface ProductComponent {
    val model: StateFlow<Any>

    fun onClickBasket()
    fun onClickAddToBasket()
    fun onClickIncrease()
    fun onClickProduct()
    fun onClickBack()
}