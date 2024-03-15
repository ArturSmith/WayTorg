package com.way_torg.myapplication.presentation.order

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.OrderStatus
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.domain.use_case.DeleteOrderUseCase
import com.way_torg.myapplication.domain.use_case.EditOrderUseCase
import com.way_torg.myapplication.domain.use_case.GetOrdersUseCase
import com.way_torg.myapplication.presentation.order.OrdersStore.Intent
import com.way_torg.myapplication.presentation.order.OrdersStore.Label
import com.way_torg.myapplication.presentation.order.OrdersStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OrdersStore : Store<Intent, State, Label> {

    data class State(
        val allOrders: List<Order>,
        val selectedOrders: List<Order>,
        val tabs: List<OrderStatus>,
        val selectedTab: OrderStatus,
        val showModalSheet: Order?
    ) {
        val isProductEditingEnable: Boolean
            get() = showModalSheet?.status == OrderStatus.UNPAID
    }

    sealed interface Intent {
        data class OnClickTab(val status: OrderStatus) : Intent
        data class OnClickOrder(val order: Order) : Intent
        data object OnClickCloseModalSheet : Intent
        data class ChangeStatus(val order: Order, val status: OrderStatus) : Intent
        data class OnClickIncreaseQuantity(val order: Order, val product: ProductWrapper) : Intent
        data class OnClickDecreaseQuantity(val order: Order, val product: ProductWrapper) : Intent
        data class OnClickDeleteProduct(val order: Order, val product: ProductWrapper) : Intent
        data class OnClickDeleteOrder(val order: Order) : Intent
    }

    sealed interface Label
}

class OrdersStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase
) {

    fun create(): OrdersStore =
        object : OrdersStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ChatStore",
            initialState = State(
                allOrders = emptyList(),
                selectedOrders = emptyList(),
                tabs = OrderStatus.entries,
                selectedTab = OrderStatus.UNPAID,
                showModalSheet = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class OrdersLoaded(val orders: List<Order>) : Action
    }

    private sealed interface Msg {
        data class SetAllOrders(val orders: List<Order>) : Msg
        data class SetSelectedTab(val status: OrderStatus) : Msg
        data class SetSelectedOrders(val orders: List<Order>) : Msg
        data class ShowModalSheet(val order: Order) : Msg
        data object HideModalSheet : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getOrdersUseCase().collect {
                    dispatch(Action.OrdersLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            val state = getState()
            when (intent) {
                is Intent.OnClickTab -> {
                    val orders = when (intent.status) {
                        OrderStatus.UNPAID -> state.allOrders.filter { it.status == OrderStatus.UNPAID }
                        OrderStatus.PAID -> state.allOrders.filter { it.status == OrderStatus.PAID }
                        OrderStatus.CANCELED -> state.allOrders.filter { it.status == OrderStatus.CANCELED }
                        OrderStatus.DELAYED -> state.allOrders.filter { it.status == OrderStatus.DELAYED }
                    }.sortedByDescending { it.orderDate }
                    dispatch(Msg.SetSelectedTab(intent.status))
                    dispatch(Msg.SetSelectedOrders(orders))
                }

                is Intent.OnClickOrder -> {
                    dispatch(Msg.ShowModalSheet(intent.order))
                }

                Intent.OnClickCloseModalSheet -> {
                    dispatch(Msg.HideModalSheet)
                }

                is Intent.ChangeStatus -> {
                    scope.launch {
                        if (intent.order.status != intent.status) {
                            editOrderUseCase(intent.order.changeStatus(intent.status))
                            dispatch(Msg.HideModalSheet)
                        }
                    }
                }

                is Intent.OnClickDecreaseQuantity -> {
                    scope.launch {
                        if (intent.product.quantity > 1) {
                            val newOrder =
                                intent.order.decreaseQuantityOfProduct(intent.product.product.id)
                            editOrderUseCase(newOrder)
                            dispatch(Msg.ShowModalSheet(newOrder))
                        }
                    }
                }

                is Intent.OnClickDeleteProduct -> {
                    scope.launch {
                        if (intent.order.products.size == 1) {
                            deleteOrderUseCase(intent.order.id)
                            dispatch(Msg.HideModalSheet)
                        } else {
                            val newOrder = intent.order.deleteProduct(intent.product)
                            editOrderUseCase(newOrder)
                            dispatch(Msg.ShowModalSheet(newOrder))
                        }
                    }
                }

                is Intent.OnClickIncreaseQuantity -> {
                    scope.launch {
                        val newOrder =
                            intent.order.increaseQuantityOfProduct(intent.product.product.id)
                        editOrderUseCase(newOrder)
                        dispatch(Msg.ShowModalSheet(newOrder))
                    }
                }

                is Intent.OnClickDeleteOrder -> {
                    scope.launch {
                        deleteOrderUseCase(intent.order.id)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.OrdersLoaded -> {
                    dispatch(Msg.SetAllOrders(action.orders))
                    val selectedOrders = action.orders
                        .filter { it.status == getState().selectedTab }
                        .sortedByDescending { it.orderDate }
                    dispatch(Msg.SetSelectedOrders(selectedOrders))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetAllOrders -> {
                    copy(allOrders = msg.orders)
                }

                is Msg.SetSelectedTab -> {
                    copy(
                        selectedTab = msg.status
                    )
                }

                is Msg.SetSelectedOrders -> {
                    copy(
                        selectedOrders = msg.orders
                    )
                }

                is Msg.ShowModalSheet -> {
                    copy(showModalSheet = msg.order)
                }

                Msg.HideModalSheet -> {
                    copy(showModalSheet = null)
                }

            }
    }
}
