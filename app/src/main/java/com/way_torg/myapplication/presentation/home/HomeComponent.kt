package com.way_torg.myapplication.presentation.home

import com.way_torg.myapplication.domain.entity.Filter
import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val model: StateFlow<HomeStore.State>

    fun onClickProduct(product: Product)
    fun onClickBasket()
    fun onClickChat()
    fun onClickCreateProduct()
    fun onClickChangeFilterState(filter: Filter)
    fun onClickAddToBasket(product: Product)

}