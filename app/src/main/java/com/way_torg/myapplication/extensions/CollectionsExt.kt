package com.way_torg.myapplication.extensions

import android.net.Uri
import androidx.compose.runtime.Composable
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.presentation.home.HomeStore
import org.checkerframework.checker.units.qual.K

fun List<HomeStore.State.ProductItem>.filterByCategory(categories: List<Category>): List<HomeStore.State.ProductItem> {
    return if (categories.isEmpty()) this
    else this.filter { product -> categories.any { it.id == product.product.category.id } }
}

fun List<Category>.filterBySelectedCategories(selectedList: List<Category>): List<Category> {
    return if (selectedList.isEmpty()) this else filter { cat -> !selectedList.any { it.id == cat.id } }
}

@Composable
inline fun <T, K> Map<T, K>.ifNotEmpty(
    ifYes: @Composable (list: Map<T, K>) -> Unit = {},
    ifNot: @Composable (list: Map<T, K>) -> Unit
) {
    if (isNotEmpty()) {
        ifNot(this)
    } else {
        ifYes(this)
    }
}




fun List<Product>.toWrapper() = this.map {
    ProductWrapper(
        product = it,
        quantity = 1
    )
}

fun List<CustomerInfo>.firstOrDefault(): CustomerInfo {
    return if (this.isNotEmpty()) this.first()
    else CustomerInfo.defaultInstance
}


fun List<Uri>.addNew(pictures: List<Uri>): List<Uri> {
    val new = this.toMutableList()
    new.addAll(pictures)
    return new.toList()
}

fun List<Product>.convertToProductItems(productsInBasket: List<String>) =
    this.map { HomeStore.State.ProductItem(it, productsInBasket.contains(it.id)) }

