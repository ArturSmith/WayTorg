package com.way_torg.myapplication.presentation.root


import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.way_torg.myapplication.presentation.basket.BasketContent
import com.way_torg.myapplication.presentation.order.ChatContent
import com.way_torg.myapplication.presentation.create_product.CreateProductContent
import com.way_torg.myapplication.presentation.details.DetailsContent
import com.way_torg.myapplication.presentation.home.HomeContent
import com.way_torg.myapplication.presentation.logo.LogoContent


@Composable
fun RootContent(
    component: DefaultRootComponent
) {
    Scaffold {
        Content(component, Modifier.padding(it))
    }
}

@OptIn(FaultyDecomposeApi::class)
@Composable
private fun Content(component: DefaultRootComponent, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Children(
            stack = component.stack,
            animation = stackAnimation { child, _, _ ->
                when (child.instance) {
                    is RootComponent.Child.CreateProduct -> {
                        slide(orientation = Orientation.Horizontal)
                    }

                    is RootComponent.Child.Details -> {
                        slide(orientation = Orientation.Vertical)
                    }

                    is RootComponent.Child.Home -> {
                        fade()
                    }

                    is RootComponent.Child.Basket -> {
                        slide()
                    }

                    is RootComponent.Child.Chat -> {
                        slide()
                    }

                    is RootComponent.Child.Logo -> {
                        slide()
                    }
                }
            }
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

                is RootComponent.Child.Details -> {
                    DetailsContent(
                        component = instance.component
                    )
                }

                is RootComponent.Child.Basket -> {
                    BasketContent(component = instance.component)
                }

                is RootComponent.Child.Chat -> {
                    ChatContent(component = instance.component)
                }

                is RootComponent.Child.Logo -> {
                    LogoContent(instance.component)
                }
            }
        }
    }
}

