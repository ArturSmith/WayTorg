package com.way_torg.myapplication.presentation.order

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.OrderStatus
import com.way_torg.myapplication.domain.entity.ProductWrapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi

class DefaultOrdersComponent @AssistedInject constructor(
    private val storeFactory: OrdersStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onClickBack") private val onClickBack: () -> Unit
) : OrdersComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    override fun onClickBack() {
        onClickBack.invoke()
    }

    override fun onClickTab(status: OrderStatus) {
        store.accept(OrdersStore.Intent.OnClickTab(status))
    }

    override fun onClickOrder(order: Order) {
        store.accept(OrdersStore.Intent.OnClickOrder(order))
    }

    override fun closeModalSheet() {
        store.accept(OrdersStore.Intent.OnClickCloseModalSheet)
    }

    override fun onClickStatus(order: Order, status: OrderStatus) {
        store.accept(OrdersStore.Intent.ChangeStatus(order, status))
    }

    override fun onClickIncreaseQuantity(order: Order, product: ProductWrapper) {
        store.accept(OrdersStore.Intent.OnClickIncreaseQuantity(order, product))
    }

    override fun onClickDecreaseQuantity(order: Order, product: ProductWrapper) {
        store.accept(OrdersStore.Intent.OnClickDecreaseQuantity(order, product))
    }

    override fun onClickDeleteProduct(order: Order, product: ProductWrapper) {
        store.accept(OrdersStore.Intent.OnClickDeleteProduct(order, product))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickBack") onClickBack: () -> Unit
        ): DefaultOrdersComponent
    }
}