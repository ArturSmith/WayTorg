package com.way_torg.myapplication.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.way_torg.myapplication.domain.entity.Filter
import com.way_torg.myapplication.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onClickCreateProduct: () -> Unit,
    private val onClickChat: () -> Unit,
    private val onClickProduct: (product:Product) -> Unit,
    private val onClickBasket: () -> Unit,
) : HomeComponent, ComponentContext by componentContext {
    override val model: StateFlow<Any>
        get() = TODO("Not yet implemented")

    override fun onClickProduct(product: Product) {
        onClickProduct.invoke(product)
    }

    override fun onClickBasket() {
        onClickBasket.invoke()
    }

    override fun onClickChat() {
        onClickChat.invoke()
    }

    override fun onClickCreateProduct() {
        onClickCreateProduct.invoke()
    }

    override fun onClickChangeFilterState(filter: Filter) {
    }

    override fun onClickAddToBasket(product: Product) {
    }
}