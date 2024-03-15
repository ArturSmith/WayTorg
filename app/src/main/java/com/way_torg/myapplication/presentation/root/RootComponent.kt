package com.way_torg.myapplication.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.way_torg.myapplication.presentation.basket.BasketComponent
import com.way_torg.myapplication.presentation.order.OrdersComponent
import com.way_torg.myapplication.presentation.create_product.CreateProductComponent
import com.way_torg.myapplication.presentation.details.DetailsComponent
import com.way_torg.myapplication.presentation.home.HomeComponent
import com.way_torg.myapplication.presentation.logo.LogoComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class CreateProduct(val component: CreateProductComponent) : Child
        data class Home(val component: HomeComponent) : Child
        data class Details(val component: DetailsComponent) : Child
        data class Basket(val component: BasketComponent) : Child
        data class Chat(val component: OrdersComponent):Child
        data class Logo(val component:LogoComponent):Child
    }
}