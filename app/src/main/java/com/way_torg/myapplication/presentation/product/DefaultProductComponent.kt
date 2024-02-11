package com.way_torg.myapplication.presentation.product

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class DefaultProductComponent(
    componentContext: ComponentContext
) : ProductComponent, ComponentContext by componentContext {
    override val model: StateFlow<Any>
        get() = TODO("Not yet implemented")

    override fun onClickBasket() {
        TODO("Not yet implemented")
    }

    override fun onClickAddToBasket() {
        TODO("Not yet implemented")
    }

    override fun onClickIncrease() {
        TODO("Not yet implemented")
    }

    override fun onClickProduct() {
        TODO("Not yet implemented")
    }

    override fun onClickBack() {
        TODO("Not yet implemented")
    }
}