package com.way_torg.myapplication.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.way_torg.myapplication.presentation.create_product.CreateProductContent
import com.way_torg.myapplication.presentation.home.HomeContent
import com.way_torg.myapplication.presentation.ui.theme.MyApplicationTheme


@Composable
fun RootContent(
    component: DefaultRootComponent
) {
    MyApplicationTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Children(
                stack = component.stack
            ) {
                when (val instance = it.instance) {
                    is RootComponent.Child.CreateProduct -> {
                        CreateProductContent(
                            component = instance.component
                        )
                    }
                    is RootComponent.Child.Home -> {
                        HomeContent(
                            component = instance.component,
                        )
                    }
                }
            }
        }
    }
}