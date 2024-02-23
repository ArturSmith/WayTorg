package com.way_torg.myapplication.presentation.home

import com.arkivanov.mvikotlin.core.store.Store
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product

interface HomeStore : Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

    data class State(
        val allProducts: List<Product>,
        val allCategories: List<Category>,
        val filteredProducts: List<Product>,
        val unselectedCategories: List<Category>,
        val selectedCategories: List<Category>,
        val productsInBasket: Int
    )

    sealed interface Label

    sealed interface Intent {
        data class OnClickUnselectedCategory(val category: Category) : Intent
        data class OnClickSelectedCategory(val category: Category) : Intent
        data class OnClickAddToBasket(val product: Product) : Intent
    }
}