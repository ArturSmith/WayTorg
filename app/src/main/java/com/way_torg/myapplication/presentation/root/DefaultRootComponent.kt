package com.way_torg.myapplication.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.way_torg.myapplication.presentation.create_product.DefaultCreateProductComponent
import com.way_torg.myapplication.presentation.home.DefaultHomeComponent
import kotlinx.android.parcel.Parcelize

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child
    )

    fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.CreateProduct -> {
                val component = DefaultCreateProductComponent(
                    componentContext = componentContext,
                    onProductSaved = {
                        navigation.pop()
                    },
                    onClickBack = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.CreateProduct(component = component)
            }

            Config.Home -> {
                val component = DefaultHomeComponent(
                    componentContext = componentContext,
                    onClickCreateProduct = {
                        navigation.push(Config.CreateProduct)
                    },
                    onClickBasket = {},
                    onClickChat = {},
                    onClickProduct = {}
                )
                RootComponent.Child.Home(component = component)
            }
        }
    }

    @Parcelize
    sealed interface Config : Parcelable {
        @Parcelize
        data object Home : Config

        @Parcelize
        data object CreateProduct : Config
    }
}