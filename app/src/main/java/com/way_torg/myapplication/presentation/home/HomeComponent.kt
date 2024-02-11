package com.way_torg.myapplication.presentation.home

import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val model: StateFlow<Any>

    fun onClickProduct()
    fun onClickBasket()
    fun onClickChat()
    fun onClickCreateProduct()

}