package com.way_torg.myapplication.presentation.home

import android.content.Context
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val model: StateFlow<HomeStore.State>


    fun onClickProduct(product: Product)
    fun onClickBasket()
    fun onClickChat()
    fun onClickCreateProduct()
    fun onClickSelectedCategory(category: Category)
    fun onClickUnselectedCategory(category: Category)
    fun onClickAddToBasket(product: Product)
    fun onClickAuthButton()
    fun changeAuthDialogVisibility()
    fun onClickLogin()
    fun onPasswordValueChangeListener(value:String)
    fun changeLocale(context: Context)

}