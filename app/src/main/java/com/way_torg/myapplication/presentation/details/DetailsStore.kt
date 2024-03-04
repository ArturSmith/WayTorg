package com.way_torg.myapplication.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.use_case.AddProductToBasketUseCase
import com.way_torg.myapplication.domain.use_case.GetAllProductsUseCase
import com.way_torg.myapplication.domain.use_case.GetCountProductsFromBasketUseCase
import com.way_torg.myapplication.domain.use_case.getAuthStateUseCase
import com.way_torg.myapplication.presentation.details.DetailsStore.Intent
import com.way_torg.myapplication.presentation.details.DetailsStore.Label
import com.way_torg.myapplication.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject


interface DetailsStore : Store<Intent, State, Label> {

    data class State(
        val product: Product,
        val products: List<Product>,
        val productsInBasket: Int,
        val isAdmin: Boolean
    )

    sealed interface Intent {
        data class OnClickAddToBasket(val product: Product) : Intent
    }

    sealed interface Label
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getCountProductsFromBasketUseCase: GetCountProductsFromBasketUseCase,
    private val addProductToBasketUseCase: AddProductToBasketUseCase,
    private val authStateUseCase: getAuthStateUseCase
) {

    fun create(product: Product): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                product = product,
                products = emptyList(),
                productsInBasket = 0,
                isAdmin = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class ProductsLoaded(val products: List<Product>) : Action
        data class CountOfProductsInBasketLoaded(val count: Int) : Action
        data class AuthStateLoaded(val authState: Boolean) : Action
    }

    private sealed interface Msg {
        data class SetProducts(val products: List<Product>) : Msg
        data class SetCountOfProducts(val count: Int) : Msg
        data class SetAuthState(val authState: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getAllProductsUseCase().collect {
                    dispatch(Action.ProductsLoaded(it))
                }
            }
            scope.launch {
                getCountProductsFromBasketUseCase().collect {
                    dispatch(Action.CountOfProductsInBasketLoaded(it))
                }
            }
            scope.launch {
                authStateUseCase().collect {
                    dispatch(Action.AuthStateLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnClickAddToBasket -> {
                    scope.launch {
                        addProductToBasketUseCase(intent.product)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            val state = getState()
            when (action) {
                is Action.CountOfProductsInBasketLoaded -> {
                    dispatch(Msg.SetCountOfProducts(action.count))
                }

                is Action.ProductsLoaded -> {
                    val filteredProducts = action.products.toMutableList()
                    filteredProducts.remove(state.product)
                    dispatch(Msg.SetProducts(filteredProducts.toList()))
                }

                is Action.AuthStateLoaded -> {
                    dispatch(Msg.SetAuthState(action.authState))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetCountOfProducts -> {
                    copy(productsInBasket = msg.count)
                }

                is Msg.SetProducts -> {
                    copy(products = msg.products)
                }

                is Msg.SetAuthState -> {
                    copy(
                        isAdmin = msg.authState
                    )
                }
            }
    }

}
