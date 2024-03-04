package com.way_torg.myapplication.presentation.details

import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val model: StateFlow<DetailsStore.State>

    fun onClickBasket()
    fun onClickAddToBasket(product: Product)
    fun onClickProduct(product: Product)
    fun onClickBack()
    fun onClickEditProduct()
}