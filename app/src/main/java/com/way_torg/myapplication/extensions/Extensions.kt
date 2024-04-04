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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ComponentContext.componentScope(): CoroutineScope = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob())
    .apply { lifecycle.doOnDestroy { cancel() } }

fun CreateProductStore.State.asInitial() = (this as CreateProductStore.State.Initial)

fun Long.convertToDataFormat(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(this))




