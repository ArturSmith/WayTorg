package com.way_torg.myapplication.presentation.root

import com.way_torg.myapplication.presentation.create_product.CreateProductComponent
import com.way_torg.myapplication.presentation.home.HomeComponent

interface RootComponent {

    sealed interface Child {
        data class CreateProduct(val component: CreateProductComponent) : Child
        data class Home(val component: HomeComponent) : Child
    }
}