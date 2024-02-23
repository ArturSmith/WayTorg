package com.way_torg.myapplication.presentation.basket

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.domain.entity.CustomerInfo
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.domain.use_case.DeleteProductFromBasketUseCase
import com.way_torg.myapplication.domain.use_case.GetProductsByIdUseCase
import com.way_torg.myapplication.domain.use_case.GetProductsFromBasketUseCase
import com.way_torg.myapplication.extensions.wrap
import com.way_torg.myapplication.presentation.basket.BasketStore.Intent
import com.way_torg.myapplication.presentation.basket.BasketStore.Label
import com.way_torg.myapplication.presentation.basket.BasketStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BasketStore : Store<Intent, State, Label> {

    sealed interface State {
        data class Initial(
            val products: List<ProductWrapper>,
            val isSheetVisible: Boolean,
            val customerInfo: CustomerInfo,
            val isCustomerNameError: Boolean,
            val isCustomerContactError: Boolean
        ) : State {
            val isOrderingEnable = products.isNotEmpty()
            val totalPriceWithDiscount = products.map { it.getTotalPriceWithDiscount() }.sum()
            val totalPriceWithoutDiscount = products.map { it.getTotalPriceWithoutDiscount() }.sum()
            val getDiscount = products.map { it.getDiscount() }.sum()
        }

        data object Loading : State

    }

    sealed interface Intent {
        data class IncreaseQuantity(val productId: String) : Intent
        data class DecreaseQuantity(val productId: String) : Intent

        data class Delete(val productId: String) : Intent
        data object Order : Intent
        data object HideSheet : Intent
        data object ShowSheet:Intent
    }

    sealed interface Label
}

class BasketStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getProductsFromBasketUseCase: GetProductsFromBasketUseCase,
    private val getProductsByIdUseCase: GetProductsByIdUseCase,
    private val deleteProductFromBasketUseCase: DeleteProductFromBasketUseCase,
) {

    fun create(): BasketStore =
        object : BasketStore, Store<Intent, State, Label> by storeFactory.create(
            name = "BasketStore",
            initialState = State.Initial(
                products = emptyList(),
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
    }

    private sealed interface Msg {
        data class SetProducts(val products: List<ProductWrapper>) : Msg
        data class IncreaseQuantity(val productId: String) : Msg
        data class DecreaseQuantity(val productId: String) : Msg
        data object Loading : Msg
        data object HideSheet : Msg
        data object ShowSheet : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getProductsFromBasketUseCase().collect {
                    dispatch(Action.ProductsFromBasketLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
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
                    //
                }

                Intent.HideSheet -> {
                    dispatch(Msg.HideSheet)
                }

                Intent.ShowSheet -> {
                    dispatch(Msg.ShowSheet)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ProductsLoaded -> {
                    dispatch(Msg.SetProducts(action.products.wrap()))
                }

                is Action.ProductsFromBasketLoaded -> {
                    scope.launch {
                        val products = getProductsByIdUseCase(action.ids).wrap()
                        dispatch(Msg.SetProducts(products))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (this) {
                is State.Initial -> reduceInitial(msg)
                State.Loading -> reduceOther(msg)
            }

        private fun State.Initial.reduceInitial(msg: Msg): State =
            when (msg) {
                is Msg.DecreaseQuantity -> {
                    val mutable =
                        products.map { if (it.product.id == msg.productId) it.decreaseQuantity() else it }
                    copy(products = mutable)
                }

                is Msg.IncreaseQuantity -> {
                    val mutable =
                        products.map { if (it.product.id == msg.productId) it.increaseQuantity() else it }
                    copy(products = mutable)
                }

                is Msg.SetProducts -> {
                    copy(
                        products = msg.products
                    )
                }

                Msg.HideSheet -> {
                    copy(isSheetVisible = false)
                }

                Msg.ShowSheet -> {
                    copy(isSheetVisible = true)
                }

                Msg.Loading -> reduceOther(msg)
            }

        private fun State.reduceOther(msg: Msg): State =
            when (msg) {
                is Msg.Loading -> State.Loading
                else -> this
            }
    }


}
