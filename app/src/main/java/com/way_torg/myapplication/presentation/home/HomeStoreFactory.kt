package com.way_torg.myapplication.presentation.home

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.way_torg.myapplication.data.repository.ProductRepositoryImpl
import com.way_torg.myapplication.domain.entity.Filter
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.repository.FilterRepository
import com.way_torg.myapplication.domain.repository.ProductRepository
import com.way_torg.myapplication.domain.use_case.AddToBasketUseCase
import com.way_torg.myapplication.domain.use_case.GetAllFiltersUseCase
import com.way_torg.myapplication.domain.use_case.GetAllProductsUseCase
import com.way_torg.myapplication.domain.use_case.GetProductsFromBasketUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getAllFiltersUseCase: GetAllFiltersUseCase,
    private val addToBasketUseCase: AddToBasketUseCase,
    private val getProductsFromBasketUseCase: GetProductsFromBasketUseCase
) {

    fun create(): HomeStore =
        object : HomeStore,
            Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
                name = "HomeStore",
                initialState = HomeStore.State(
                    products = emptyList(),
                    filters = emptyList(),
                    productsInBasket = 0
                ),
                reducer = ReducerImpl,
                executorFactory = ::ExecutorImpl,
                bootstrapper = BootstrapperImpl()
            ) {}

    private sealed interface Action {
        data class ProductsLoaded(val products: List<Product>) : Action
        data class FiltersLoaded(val filters: List<Filter>) : Action
        data class CountOfProductsInBasketLoaded(val products: Int) : Action
    }

    private sealed interface Msg {

        data class ProductsLoaded(val products: List<Product>) : Msg
        data class FiltersLoaded(val filters: List<Filter>) : Msg
        data class CountOfProductsInBasketLoaded(val products: Int) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<HomeStore.Intent, Action, HomeStore.State, Msg, HomeStore.Label>() {

        override fun executeAction(action: Action, getState: () -> HomeStore.State) {
            when (action) {
                is Action.FiltersLoaded -> {
                    dispatch(Msg.FiltersLoaded(action.filters))
                }

                is Action.ProductsLoaded -> {
                    dispatch(Msg.ProductsLoaded(action.products))
                }

                is Action.CountOfProductsInBasketLoaded -> {
                    dispatch(Msg.CountOfProductsInBasketLoaded(action.products))
                }
            }
        }

        override fun executeIntent(intent: HomeStore.Intent, getState: () -> HomeStore.State) {
            when (intent) {
                is HomeStore.Intent.OnClickAddToBasket -> {
                    scope.launch {
                        addToBasketUseCase(intent.product)
                    }
                }

                HomeStore.Intent.OnClickBasket -> {
                    publish(HomeStore.Label.OnClickBasket)
                }

                is HomeStore.Intent.OnClickChangeFilterState -> {

                }

                HomeStore.Intent.OnClickChat -> {
                    publish(HomeStore.Label.OnClickChat)
                }

                HomeStore.Intent.OnClickCreateProduct -> {
                    publish(HomeStore.Label.OnClickCreateProduct)
                }

                is HomeStore.Intent.OnClickProduct -> {
                    publish(HomeStore.Label.OnClickProduct(intent.product))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<HomeStore.State, Msg> {
        override fun HomeStore.State.reduce(msg: Msg): HomeStore.State = when (msg) {
            is Msg.FiltersLoaded -> {
                copy(filters = msg.filters)
            }

            is Msg.ProductsLoaded -> {
                copy(products = msg.products)
            }

            is Msg.CountOfProductsInBasketLoaded -> {
                copy(productsInBasket = msg.products)
            }
        }
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getAllProductsUseCase().collect {
                    dispatch(Action.ProductsLoaded(it))
                }
            }
            scope.launch {
                getAllFiltersUseCase().collect {
                    dispatch(Action.FiltersLoaded(it))
                }
            }
            scope.launch {
                getProductsFromBasketUseCase().collect {
                    dispatch(Action.CountOfProductsInBasketLoaded(it.size))
                }
            }
        }
    }
}