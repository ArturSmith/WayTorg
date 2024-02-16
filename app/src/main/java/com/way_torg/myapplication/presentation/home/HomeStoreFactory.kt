package com.way_torg.myapplication.presentation.home

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.use_case.AddProductToBasketUseCase
import com.way_torg.myapplication.domain.use_case.GetAllCategoriesFromRemoteDbUseCase
import com.way_torg.myapplication.domain.use_case.GetAllProductsUseCase
import com.way_torg.myapplication.domain.use_case.GetAllSelectedCategoriesFromLocalDbUseCase
import com.way_torg.myapplication.domain.use_case.GetCountProductsFromBasketUseCase
import com.way_torg.myapplication.domain.use_case.SelectCategoryUseCase
import com.way_torg.myapplication.domain.use_case.UnselectCategoryUseCase
import com.way_torg.myapplication.extensions.filterByCategory
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getCountProductsFromBasketUseCase: GetCountProductsFromBasketUseCase,
    private val addProductToBasketUseCase: AddProductToBasketUseCase,
    private val getAllCategoriesFromRemoteDbUseCase: GetAllCategoriesFromRemoteDbUseCase,
    private val getSelectedCategoryUseCase: GetAllSelectedCategoriesFromLocalDbUseCase,
    private val selectCategoryUseCase: SelectCategoryUseCase,
    private val unselectCategoryUseCase: UnselectCategoryUseCase
) {

    fun create(): HomeStore =
        object : HomeStore,
            Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
                name = "HomeStore",
                initialState = HomeStore.State(
                    products = emptyList(),
                    unselectedCategories = emptyList(),
                    selectedCategories = emptyList(),
                    productsInBasket = 0
                ),
                reducer = ReducerImpl,
                executorFactory = ::ExecutorImpl,
                bootstrapper = BootstrapperImpl()
            ) {}

    private sealed interface Action {
        data class ProductsLoaded(val products: List<Product>) : Action
        data class AllCategoriesLoaded(val categories: List<Category>) : Action
        data class SelectedCategoriesLoaded(val categories: List<Category>) : Action
        data class CountOfProductsInBasketLoaded(val products: Int) : Action
    }

    private sealed interface Msg {

        data class ProductsLoaded(val products: List<Product>) : Msg
        data class AllCategoriesLoaded(val categories: List<Category>) : Msg
        data class SelectedCategoriesLoaded(val categories: List<Category>) : Msg
        data class CountOfProductsInBasketLoaded(val products: Int) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<HomeStore.Intent, Action, HomeStore.State, Msg, HomeStore.Label>() {

        override fun executeAction(action: Action, getState: () -> HomeStore.State) {
            val state = getState()
            when (action) {
                is Action.AllCategoriesLoaded -> {
                    dispatch(Msg.AllCategoriesLoaded(action.categories))
                }

                is Action.SelectedCategoriesLoaded -> {
                    dispatch(Msg.SelectedCategoriesLoaded(action.categories))
                }

                is Action.ProductsLoaded -> {
                    dispatch(Msg.ProductsLoaded(action.products.filterByCategory(state.selectedCategories)))
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
                        addProductToBasketUseCase(intent.product)
                    }
                }

                HomeStore.Intent.OnClickBasket -> {
                    publish(HomeStore.Label.OnClickBasket)
                }

                is HomeStore.Intent.OnClickUnselectedCategory -> {
                    scope.launch {
                        selectCategoryUseCase(intent.category)
                    }
                }

                is HomeStore.Intent.OnClickSelectedCategory -> {
                    scope.launch {
                        unselectCategoryUseCase(intent.category.id)
                    }
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
            is Msg.CountOfProductsInBasketLoaded -> {
                copy(productsInBasket = msg.products)
            }

            is Msg.ProductsLoaded -> {
                copy(products = msg.products)
            }

            is Msg.SelectedCategoriesLoaded -> {
                copy(selectedCategories = msg.categories)
            }

            is Msg.AllCategoriesLoaded -> {
                val filterCategories =
                    msg.categories.filter { !this.selectedCategories.contains(it) }
                copy(unselectedCategories = filterCategories)
            }
        }
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getCountProductsFromBasketUseCase().collect {
                    dispatch(Action.CountOfProductsInBasketLoaded(it))
                }
            }
            scope.launch {
                getSelectedCategoryUseCase().collect {
                    dispatch(Action.SelectedCategoriesLoaded(it))
                }
            }
            scope.launch {
                getAllProductsUseCase()
                    .collect {
                        dispatch(Action.ProductsLoaded(it))
                    }
            }
            scope.launch {
                getAllCategoriesFromRemoteDbUseCase().collect {
                    dispatch(Action.AllCategoriesLoaded(it))
                }
            }
        }
    }
}

