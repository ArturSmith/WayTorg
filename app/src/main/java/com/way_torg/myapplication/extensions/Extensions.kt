package com.way_torg.myapplication.extensions

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


fun List<Product>.filterByCategory(list: List<Category>): List<Product> {
    return if (list.isEmpty()) this else this.filter { list.contains(it.category) }
}
