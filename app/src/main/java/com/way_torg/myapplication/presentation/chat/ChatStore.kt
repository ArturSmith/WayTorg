package com.way_torg.myapplication.presentation.chat

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.use_case.EditOrderUseCase
import com.way_torg.myapplication.domain.use_case.GetOrdersUseCase
import com.way_torg.myapplication.presentation.chat.ChatStore.Intent
import com.way_torg.myapplication.presentation.chat.ChatStore.Label
import com.way_torg.myapplication.presentation.chat.ChatStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ChatStore : Store<Intent, State, Label> {

    data class State(
        val allOrders: List<Order>,
        val tabs: List<Order.Status>,
        val selectedTab: SelectedTab
    ) {
        data class SelectedTab(
            val selectedOrders: List<Order>,
            val index: Int
        )
    }

    sealed interface Intent {
        data class OnClickFilter(val status: Order.Status) : Intent
        data object OnClickOrder : Intent
        data class OnClickStatus(val order: Order, val status: Int) : Intent
    }

    sealed interface Label
}

class ChatStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val editOrderUseCase: EditOrderUseCase
) {

    fun create(): ChatStore =
        object : ChatStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ChatStore",
            initialState = State(
                allOrders = emptyList(),
                tabs = Order.Status.entries,
                selectedTab = State.SelectedTab(
                    emptyList(),
                    Order.Status.UNPAID.index
                )
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
        data class SetSelectedTab(val status: Order.Status, val orders: List<Order>) : Msg
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

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            val state = getState()
            when (intent) {
                is Intent.OnClickFilter -> {
                    val orders = when (intent.status) {
                        Order.Status.UNPAID -> state.allOrders.filter { it.status == Order.Status.UNPAID.index }
                        Order.Status.PAID -> state.allOrders.filter { it.status == Order.Status.PAID.index }
                        Order.Status.CANCELED -> state.allOrders.filter { it.status == Order.Status.CANCELED.index }
                        Order.Status.DELAYED -> state.allOrders.filter { it.status == Order.Status.DELAYED.index }
                    }
                    dispatch(Msg.SetSelectedTab(intent.status, orders))
                }

                Intent.OnClickOrder -> TODO()
                is Intent.OnClickStatus -> TODO()
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.OrdersLoaded -> {
                    dispatch(Msg.SetAllOrders(action.orders))
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
                    Log.d("Tabs G", msg.status.toString())
                    copy(
                        selectedTab = State.SelectedTab(
                            selectedOrders = msg.orders,
                            index = msg.status.index
                        )
                    )

                }
            }
    }
}
