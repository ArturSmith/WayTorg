package com.way_torg.myapplication.extensions

import androidx.compose.runtime.Composable
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.presentation.basket.BasketStore

fun List<Product>.filterByCategory(categories: List<Category>): List<Product> {
    return if (categories.isEmpty()) this else this.filter { product -> categories.any { it.id == product.category.id } }
}

fun List<Category>.filterBySelectedCategories(selectedList: List<Category>): List<Category> {
    return if (selectedList.isEmpty()) this else filter { cat -> !selectedList.any { it.id == cat.id } }
}

@Composable
fun <T> Collection<T>.ifNotEmpty(
    ifYes: @Composable (list: Collection<T>) -> Unit = {},
    ifNot: @Composable (list: Collection<T>) -> Unit
) {
    if (isNotEmpty()) {
        ifNot(this)
    } else {
        ifYes(this)
    }
}


fun List<Product>.wrap() = this.map {
    ProductWrapper(
        product = it,
        quantityInBasket = 1,
        totalPrice = it.price
    )
}

