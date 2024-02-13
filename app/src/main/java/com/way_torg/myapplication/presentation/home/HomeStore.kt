package com.way_torg.myapplication.presentation.home

import com.arkivanov.mvikotlin.core.store.Store
import com.way_torg.myapplication.domain.entity.Filter
import com.way_torg.myapplication.domain.entity.Product

interface HomeStore : Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

    data class State(
        val products: List<Product>,
        val filters: List<Filter>,
        val productsInBasket: Int
    )

    sealed interface Label {
        data class OnClickProduct(val product: Product) : Label
        data object OnClickBasket : Label
        data object OnClickChat : Label
        data object OnClickCreateProduct : Label
    }

    sealed interface Intent {
        data class OnClickProduct(val product: Product) : Intent
        data object OnClickBasket : Intent
        data object OnClickChat : Intent
        data object OnClickCreateProduct : Intent
        data class OnClickChangeFilterState(val filter: Filter) : Intent
        data class OnClickAddToBasket(val product: Product) : Intent
    }
}