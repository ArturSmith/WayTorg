package com.way_torg.myapplication.presentation.create_product

import kotlinx.coroutines.flow.StateFlow

interface CreateProductComponent {
    val model: StateFlow<Any>

    fun onClickSetName()
    fun onClickSetCategory()
    fun onClickSetCount()
    fun onClickSetDescription()
    fun onClickSetPrice()
    fun onClickSetDiscount()
    fun onClickAddPictures()
    fun onClickBack()
    fun onClickCreate()
}