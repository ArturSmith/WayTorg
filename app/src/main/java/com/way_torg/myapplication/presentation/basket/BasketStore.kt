package com.way_torg.myapplication.presentation.basket

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.domain.use_case.CleanBasketUseCase
import com.way_torg.myapplication.domain.use_case.DeleteProductFromBasketUseCase
import com.way_torg.myapplication.domain.use_case.GetCustomersFromDbUseCase
import com.way_torg.myapplication.domain.use_case.GetProductsByIdUseCase
import com.way_torg.myapplication.domain.use_case.GetProductsFromBasketUseCase
import com.way_torg.myapplication.domain.use_case.CreateOrderUseCase
import com.way_torg.myapplication.domain.use_case.SaveCustomerIntoDbUseCase
import com.way_torg.myapplication.extensions.firstOrDefault
import com.way_torg.myapplication.extensions.isAnyRequiredFieldEmpty
import com.way_torg.myapplication.extensions.validateFields
import com.way_torg.myapplication.extensions.toWrapper
import com.way_torg.myapplication.presentation.basket.BasketStore.Intent
import com.way_torg.myapplication.presentation.basket.BasketStore.Label
import com.way_torg.myapplication.presentation.basket.BasketStore.State
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

interface BasketStore : Store<Intent, State, Label> {

    sealed interface State {
        data class Initial(
            val order: Order,
            val isSheetVisible: Boolean,
            val customerInfo: CustomerInfo,
            val customersFromDb: List<CustomerInfo>,
            val isCustomerNameError: Boolean,
            val isCustomerContactError: Boolean
        ) : State

        data object Loading : State
        data object Success : State

    }

    sealed interface Intent {
        data class IncreaseQuantity(val productId: String) : Intent
        data class DecreaseQuantity(val productId: String) : Intent

        data class Delete(val productId: String) : Intent
        data object Order : Intent
        data object HideSheet : Intent
        data object ShowSheet : Intent

        data class SetCustomerName(val name: String) : Intent
        data class SetCustomerAddress(val address: String) : Intent
        data class SetCustomerContact(val contact: String) : Intent
        data class SetCustomerMessage(val message: String) : Intent
        data class OnClickCustomer(val customerInfo: CustomerInfo) : Intent
    }

    sealed interface Label {
        data object OrderMade : Label
    }
}

class BasketStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getProductsFromBasketUseCase: GetProductsFromBasketUseCase,
    private val getProductsByIdUseCase: GetProductsByIdUseCase,
    private val deleteProductFromBasketUseCase: DeleteProductFromBasketUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val saveCustomerIntoDbUseCase: SaveCustomerIntoDbUseCase,
    private val getCustomersFromDbUseCase: GetCustomersFromDbUseCase,
    private val cleanBasketUseCase: CleanBasketUseCase
) {

    fun create(): BasketStore =
        object : BasketStore, Store<Intent, State, Label> by storeFactory.create(
            name = "BasketStore",
            initialState = State.Initial(
                order = Order.defaultInstance.copy(id = UUID.randomUUID().toString()),
                customersFromDb = emptyList(),
                isSheetVisible = false,
                customerInfo = CustomerInfo.defaultInstance,
                isCustomerNameError = false,
                isCustomerContactError = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class ProductsFromBasketLoaded(val ids: List<String>) : Action
        data class ProductsLoaded(val products: List<Product>) : Action
        data class CustomersLoaded(val customers: List<CustomerInfo>) : Action
    }

    private sealed interface Msg {
        data class SetProducts(val products: List<ProductWrapper>) : Msg
        data class IncreaseQuantity(val productId: String) : Msg
        data class DecreaseQuantity(val productId: String) : Msg
        data class SetCustomers(val customers: List<CustomerInfo>) : Msg
        data object Loading : Msg
        data object Success : Msg
        data object HideSheet : Msg
        data object ShowSheet : Msg
        data object ValidateFields : Msg

        data class SetCustomerName(val name: String) : Msg
        data class SetCustomerAddress(val address: String) : Msg
        data class SetCustomerContact(val contact: String) : Msg
        data class SetCustomerMessage(val message: String) : Msg
        data class SetCustomer(val customerInfo: CustomerInfo) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getProductsFromBasketUseCase().collect {
                    dispatch(Action.ProductsFromBasketLoaded(it))
                }
            }
            scope.launch {
                getCustomersFromDbUseCase().collect {
                    dispatch(Action.CustomersLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            val state = getState() as State.Initial
            when (intent) {
                is Intent.DecreaseQuantity -> {
                    dispatch(Msg.DecreaseQuantity(intent.productId))
                }

                is Intent.Delete -> {
                    scope.launch {
                        deleteProductFromBasketUseCase(intent.productId)
                    }
                }

                is Intent.IncreaseQuantity -> {
                    dispatch(Msg.IncreaseQuantity(intent.productId))
                }

                Intent.Order -> {
                    scope.launch {
                        if (state.isAnyRequiredFieldEmpty()) {
                            dispatch(Msg.ValidateFields)
                        } else {
                            val customerInfo =
                                if (state.customersFromDb.contains(state.customerInfo))
                                    state.customerInfo
                                else {
                                    val newCustomerInfo =
                                        state.customerInfo.copy(id = UUID.randomUUID().toString())
                                    async { saveCustomerIntoDbUseCase(newCustomerInfo) }.await()
                                    newCustomerInfo
                                }
                            val order = state.order.copy(
                                customerInfo = customerInfo,
                                orderDate = Calendar.getInstance().timeInMillis
                            )
                            dispatch(Msg.Loading)
                            async { createOrderUseCase(order) }.await()
                            async { cleanBasketUseCase() }.await()
                            dispatch(Msg.Success)
                            delay(1000)
                            publish(Label.OrderMade)
                        }
                    }
                }

                Intent.HideSheet -> {
                    dispatch(Msg.HideSheet)
                }

                Intent.ShowSheet -> {
                    dispatch(Msg.ShowSheet)
                }

                is Intent.OnClickCustomer -> {
                    dispatch(Msg.SetCustomer(intent.customerInfo))
                }

                is Intent.SetCustomerAddress -> {
                    dispatch(Msg.SetCustomerAddress(intent.address))
                }

                is Intent.SetCustomerContact -> {
                    dispatch(Msg.SetCustomerContact(intent.contact))
                }

                is Intent.SetCustomerMessage -> {
                    dispatch(Msg.SetCustomerMessage(intent.message))
                }

                is Intent.SetCustomerName -> {
                    dispatch(Msg.SetCustomerName(intent.name))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ProductsLoaded -> {
                    dispatch(Msg.SetProducts(action.products.toWrapper()))
                }

                is Action.ProductsFromBasketLoaded -> {
                    scope.launch {
                        val products = getProductsByIdUseCase(action.ids).toWrapper()
                        dispatch(Msg.SetProducts(products))
                    }
                }

                is Action.CustomersLoaded -> {
                    dispatch(Msg.SetCustomers(action.customers))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (this) {
                is State.Initial -> reduceInitial(msg)
                State.Loading -> reduceOther(msg)
                State.Success -> reduceOther(msg)
            }

        private fun State.Initial.reduceInitial(msg: Msg): State =
            when (msg) {
                is Msg.DecreaseQuantity -> {
                    copy(order = order.decreaseQuantityOfProduct(msg.productId))
                }

                is Msg.IncreaseQuantity -> {
                    copy(order = order.increaseQuantityOfProduct(msg.productId))
                }

                is Msg.SetProducts -> {
                    copy(
                        order = order.copy(products = msg.products)
                    )
                }

                is Msg.SetCustomers -> {
                    copy(
                        customersFromDb = msg.customers,
                        customerInfo = msg.customers.firstOrDefault()
                    )
                }

                Msg.HideSheet -> {
                    copy(isSheetVisible = false)
                }

                Msg.ShowSheet -> {
                    copy(isSheetVisible = true)
                }

                is Msg.SetCustomer -> {
                    copy(customerInfo = msg.customerInfo)
                }

                is Msg.SetCustomerAddress -> {
                    copy(
                        customerInfo = customerInfo.copy(address = msg.address)
                    )
                }

                is Msg.SetCustomerContact -> {
                    copy(
                        customerInfo = customerInfo.copy(contact = msg.contact)
                    )
                }

                is Msg.SetCustomerMessage -> {
                    copy(
                        customerInfo = customerInfo.copy(message = msg.message)
                    )
                }

                is Msg.SetCustomerName -> {
                    copy(
                        customerInfo = customerInfo.copy(name = msg.name)
                    )
                }

                Msg.ValidateFields -> {
                    validateFields()
                }

                Msg.Success,
                Msg.Loading -> reduceOther(msg)
            }

        private fun State.reduceOther(msg: Msg): State =
            when (msg) {
                is Msg.Loading -> State.Loading
                is Msg.Success -> State.Success
                else -> this
            }
    }

}
