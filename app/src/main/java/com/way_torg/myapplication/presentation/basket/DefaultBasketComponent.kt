package com.way_torg.myapplication.presentation.basket

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.way_torg.myapplication.domain.entity.Product
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi

class DefaultBasketComponent @AssistedInject constructor(
    private val storeFactory: BasketStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("onClickProduct") private val onClickProduct: (product: Product) -> Unit
) : BasketComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    override fun increaseQuantity(productId: String) {
        store.accept(BasketStore.Intent.IncreaseQuantity(productId))
    }

    override fun decreaseQuantity(productId: String) {
        store.accept(BasketStore.Intent.DecreaseQuantity(productId))
    }

    override fun onClickProduct(product: Product) {
        onClickProduct.invoke(product)
    }

    override fun delete(productId: String) {
        store.accept(BasketStore.Intent.Delete(productId))
    }

    override fun order() {
        store.accept(BasketStore.Intent.Order)
    }

    override fun hideSheet() {
        store.accept(BasketStore.Intent.HideSheet)
    }

    override fun showSheet() {
        store.accept(BasketStore.Intent.ShowSheet)
    }

    override fun onClickBack() {
        onClickBack.invoke()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("onClickProduct") onClickProduct: (product: Product) -> Unit
        ): DefaultBasketComponent
    }
}