package com.way_torg.myapplication.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.way_torg.myapplication.presentation.create_product.CreateProductStoreFactory
import com.way_torg.myapplication.presentation.create_product.DefaultCreateProductComponent
import com.way_torg.myapplication.presentation.home.DefaultHomeComponent
import com.way_torg.myapplication.presentation.home.HomeComponent
import com.way_torg.myapplication.presentation.home.HomeStoreFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.android.parcel.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val homeComponentFactory: DefaultHomeComponent.Factory,
    private val createProductStoreFactory: DefaultCreateProductComponent.Factory,
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
                val component = createProductStoreFactory.create(
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
                    onClickProduct = {},
                    onClickChat = {},
                    onClickBasket = {}
                )
                RootComponent.Child.Home(component = component)
            }
        }
    }

    @Parcelize
    private sealed interface Config : Parcelable {
        @Parcelize
        data object Home : Config

        @Parcelize
        data object CreateProduct : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}