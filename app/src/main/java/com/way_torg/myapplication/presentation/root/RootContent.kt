package com.way_torg.myapplication.presentation.root

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.way_torg.myapplication.extensions.invert
import com.way_torg.myapplication.presentation.basket.BasketContent
import com.way_torg.myapplication.presentation.order.ChatContent
import com.way_torg.myapplication.presentation.create_product.CreateProductContent
import com.way_torg.myapplication.presentation.details.DetailsContent
import com.way_torg.myapplication.presentation.home.HomeContent
import com.way_torg.myapplication.presentation.ui.theme.MyApplicationTheme


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
            }
        }
    }
}

