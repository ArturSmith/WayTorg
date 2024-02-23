package com.way_torg.myapplication.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.presentation.basket.DefaultBasketComponent
import com.way_torg.myapplication.presentation.create_product.DefaultCreateProductComponent
import com.way_torg.myapplication.presentation.details.DefaultDetailsComponent
import com.way_torg.myapplication.presentation.home.DefaultHomeComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.android.parcel.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val homeComponentFactory: DefaultHomeComponent.Factory,
    private val createProductComponentFactory: DefaultCreateProductComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val basketComponentFactory: DefaultBasketComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            Config.CreateProduct -> {
                val component = createProductComponentFactory.create(
                    componentContext = componentContext,
                    onClickBack = {
                        navigation.pop()
                    },
                    onProductSaved = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.CreateProduct(component = component)
            }

            Config.Home -> {
                val component = homeComponentFactory.create(
                    componentContext = componentContext,
                    onClickCreateProduct = {
                        navigation.push(Config.CreateProduct)
                    },
                    onClickProduct = {
                        navigation.push(Config.Details(it))
                    },
                    onClickChat = {},
                    onClickBasket = {
                        navigation.push(Config.Basket)
                    }
                )
                RootComponent.Child.Home(component = component)
            }

            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    componentContext = componentContext,
                    product = config.product,
                    onClickBack = {
                        navigation.pop()
                    },
                    onClickBasket = {
                        navigation.replaceCurrent(Config.Basket)
                    },
                    onClickProduct = {
                        navigation.replaceCurrent(Config.Details(it))
                    }
                )
                RootComponent.Child.Details(component = component)
            }

            is Config.Basket -> {
                val component = basketComponentFactory.create(
                    componentContext = componentContext,
                    onClickBack = {
                        navigation.pop()
                    },
                    onClickProduct = {
                        navigation.replaceCurrent(Config.Details(it))
                    }
                )
                RootComponent.Child.Basket(component = component)
            }
        }
    }

    @Parcelize
    private sealed interface Config : Parcelable {
        @Parcelize
        data object Home : Config

        @Parcelize
        data object CreateProduct : Config

        @Parcelize
        data class Details(val product: Product) : Config

        @Parcelize
        data object Basket : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}