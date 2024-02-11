package com.way_torg.myapplication.presentation.create_product

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class DefaultCreateProductComponent(
    componentContext: ComponentContext,
    private val onProductSaved: () -> Unit,
    val onClickBack: () -> Unit,
) : CreateProductComponent, ComponentContext by componentContext {
    override val model: StateFlow<Any>
        get() = TODO("Not yet implemented")

    override fun onClickSetName() {
        TODO("Not yet implemented")
    }

    override fun onClickSetCategory() {
        TODO("Not yet implemented")
    }

    override fun onClickSetCount() {
        TODO("Not yet implemented")
    }

    override fun onClickSetDescription() {
        TODO("Not yet implemented")
    }

    override fun onClickSetPrice() {
        TODO("Not yet implemented")
    }

    override fun onClickSetDiscount() {
        TODO("Not yet implemented")
    }

    override fun onClickAddPictures() {
        TODO("Not yet implemented")
    }

    override fun onClickBack() {
        onClickBack.invoke()
    }

    override fun onClickCreate() {
        TODO("Not yet implemented")
    }
}