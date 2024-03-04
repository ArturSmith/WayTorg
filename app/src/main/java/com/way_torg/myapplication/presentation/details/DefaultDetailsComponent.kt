package com.way_torg.myapplication.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.way_torg.myapplication.domain.entity.Product
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("product") private val product: Product,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onClickEditProduct") private val onClickEditProduct: (product: Product) -> Unit,
    @Assisted("onClickBasket") private val onClickBasket: () -> Unit,
    @Assisted("onClickProduct") private val onClickProduct: (product: Product) -> Unit,
    @Assisted("onClickBack") private val onClickBack: () -> Unit
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(product) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow


    override fun onClickBasket() {
        onClickBasket.invoke()
    }

    override fun onClickAddToBasket(product: Product) {
        store.accept(DetailsStore.Intent.OnClickAddToBasket(product))
    }

    override fun onClickProduct(product: Product) {
        onClickProduct.invoke(product)
    }

    override fun onClickBack() {
        onClickBack.invoke()
    }

    override fun onClickEditProduct() {
        onClickEditProduct.invoke(product)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickEditProduct") onClickEditProduct: (product: Product) -> Unit,
            @Assisted("onClickBasket") onClickBasket: () -> Unit,
            @Assisted("onClickProduct") onClickProduct: (product: Product) -> Unit,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("product") product: Product
        ): DefaultDetailsComponent
    }
}