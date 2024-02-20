package com.way_torg.myapplication.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DefaultHomeComponent @AssistedInject constructor(
    private val storeFactory: HomeStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onClickCreateProduct") private val onClickCreateProduct: () -> Unit,
    @Assisted("onClickChat") private val onClickChat: () -> Unit,
    @Assisted("onClickProduct") private val onClickProduct: (product: Product) -> Unit,
    @Assisted("onClickBasket") private val onClickBasket: () -> Unit
) : HomeComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HomeStore.State>
        get() = store.stateFlow



    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    HomeStore.Label.OnClickBasket -> {
                        onClickBasket.invoke()
                    }

                    HomeStore.Label.OnClickChat -> {
                        onClickChat.invoke()
                    }

                    HomeStore.Label.OnClickCreateProduct -> {
                        onClickCreateProduct.invoke()
                    }

                    is HomeStore.Label.OnClickProduct -> {
                        onClickProduct.invoke(it.product)
                    }
                }
            }
        }
    }


    override fun onClickProduct(product: Product) {
        store.accept(HomeStore.Intent.OnClickProduct(product))
    }

    override fun onClickBasket() {

        store.accept(HomeStore.Intent.OnClickBasket)
    }

    override fun onClickChat() {
        store.accept(HomeStore.Intent.OnClickChat)
    }

    override fun onClickCreateProduct() {
        store.accept(HomeStore.Intent.OnClickCreateProduct)
    }

    override fun onClickSelectedCategory(category: Category) {
        store.accept(HomeStore.Intent.OnClickSelectedCategory(category))
    }

    override fun onClickUnselectedCategory(category: Category) {
        store.accept(HomeStore.Intent.OnClickUnselectedCategory(category))
    }

    override fun onClickAddToBasket(product: Product) {
        store.accept(HomeStore.Intent.OnClickAddToBasket(product))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickCreateProduct") onClickCreateProduct: () -> Unit,
            @Assisted("onClickChat") onClickChat: () -> Unit,
            @Assisted("onClickProduct") onClickProduct: (product: Product) -> Unit,
            @Assisted("onClickBasket") onClickBasket: () -> Unit
        ): DefaultHomeComponent
    }
}