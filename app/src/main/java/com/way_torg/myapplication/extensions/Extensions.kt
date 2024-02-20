package com.way_torg.myapplication.extensions

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.presentation.create_product.CreateProductStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentScope(): CoroutineScope = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

fun CreateProductStore.State.asInitial() = (this as CreateProductStore.State.Initial)

fun String.toNewInt(): Int {
    return if (this.isEmpty()) {
        0
    } else {
        this.toInt()
    }
}

fun String.toNewDouble(): Double {
    return if (this.isEmpty()) {
        0.0
    } else {
        this.toDouble()
    }
}


fun List<Product>.filterByCategory(categories: List<Category>): List<Product> {
    return if (categories.isEmpty()) this else this.filter { product -> categories.any { it.id == product.category.id } }
}


fun List<Category>.filterBySelectedCategories(selectedList: List<Category>): List<Category> {
    return if (selectedList.isEmpty()) this else filter { cat -> !selectedList.any { it.id == cat.id } }
}

@Composable
fun <T> Collection<T>.IfNotEmpty(predicate: @Composable () -> Unit) {
    if (isNotEmpty()) {
        predicate()
    }
}


suspend fun String.getOrCreateCategory(
    categories: List<Category>,
    predicate: suspend (newCategory: String) -> Category
) = categories.find { it.name == this } ?: predicate(this)




