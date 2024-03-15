package com.way_torg.myapplication.presentation.root

import android.os.Parcelable
import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.presentation.basket.DefaultBasketComponent
import com.way_torg.myapplication.presentation.order.DefaultOrdersComponent
import com.way_torg.myapplication.presentation.create_product.DefaultCreateProductComponent
import com.way_torg.myapplication.presentation.details.DefaultDetailsComponent
import com.way_torg.myapplication.presentation.home.DefaultHomeComponent
import com.way_torg.myapplication.presentation.logo.DefaultLogoComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
class DefaultRootComponent @AssistedInject constructor(
    private val homeComponentFactory: DefaultHomeComponent.Factory,
    private val createProductComponentFactory: DefaultCreateProductComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val basketComponentFactory: DefaultBasketComponent.Factory,
    private val chatComponentFactory: DefaultOrdersComponent.Factory,
    private val logoComponentFactory: DefaultLogoComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Logo,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (val localConfig = config) {
            is Config.CreateProduct -> {
                val component = createProductComponentFactory.create(
                    product = localConfig.product,
                    componentContext = componentContext,
                    onClickBack = {
                        navigation.pop()
                    },
                    onProductSaved = {
                        navigation.pop()
                    },
                    onNavigateHome = {
                        navigation.popTo(0)
                    }
                )
                RootComponent.Child.CreateProduct(component = component)
            }

            is Config.Home -> {
                val component = homeComponentFactory.create(
                    componentContext = componentContext,
                    onClickCreateProduct = {
                        navigation.push(Config.CreateProduct(null))
                    },
                    onClickProduct = {
                        navigation.push(Config.Details(it))
                    },
                    onClickChat = {
                        navigation.push(Config.Chat)
                    },
                    onClickBasket = {
                        navigation.push(Config.Basket)
                    }
                )
                RootComponent.Child.Home(component = component)
            }

            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    componentContext = componentContext,
                    product = localConfig.product,
                    onClickBack = {
                        navigation.pop()
                    },
                    onClickBasket = {
                        navigation.replaceCurrent(Config.Basket)
                    },
                    onClickProduct = {
                        navigation.replaceCurrent(Config.Details(it))
                    },
                    onClickEditProduct = {
                        navigation.push(Config.CreateProduct(it))
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

            Config.Chat -> {
                val component = chatComponentFactory.create(
                    componentContext = componentContext,
                    onClickBack = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.Chat(component = component)
            }

            Config.Logo -> {
                val component = logoComponentFactory.create(
                    componentContext = componentContext
                ) {
                    navigation.replaceCurrent(Config.Home)
                }
                RootComponent.Child.Logo(component)
            }
        }
    }

    @Parcelize
    private sealed interface Config : Parcelable {
        @Parcelize
        data object Home : Config

        @Parcelize
        data class CreateProduct(val product: Product?) : Config

        @Parcelize
        data class Details(val product: Product) : Config

        @Parcelize
        data object Basket : Config

        @Parcelize
        data object Chat : Config

        @Parcelize
        data object Logo : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}