package com.way_torg.myapplication.presentation.home

import com.arkivanov.mvikotlin.core.store.Store
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product

interface HomeStore : Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

    data class State(
        val allProducts: List<ProductItem>,
        val allCategories: List<Category>,
        val filteredProducts: List<ProductItem>,
        val unselectedCategories: List<Category>,
        val selectedCategories: List<Category>,
        val productsInBasket: List<String>,
        val isContentVisible: Boolean,
        val password: String,
        val isAuthDialogVisible: Boolean,
        val authState: Boolean,
        val countOfUnpaidOrders:Int
    ) {
        data class ProductItem(
            val product: Product,
            val isInBasket: Boolean
        )
    }

    sealed interface Label

    sealed interface Intent {
        data class OnClickUnselectedCategory(val category: Category) : Intent
        data class OnClickSelectedCategory(val category: Category) : Intent
        data class OnClickAddToBasket(val product: Product) : Intent
        data object OnClickAuthButton : Intent
        data object ChangeAuthDialogVisibility : Intent
        data class OnPasswordValueChangeListener(val value: String) : Intent
        data object OnClickLogin : Intent
    }
}