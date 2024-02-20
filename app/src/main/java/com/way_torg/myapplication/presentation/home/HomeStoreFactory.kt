package com.way_torg.myapplication.presentation.home

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
import com.way_torg.myapplication.extensions.filterBySelectedCategories
import kotlinx.coroutines.async
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
                    allProducts = emptyList(),
                    allCategories = emptyList(),
                    filteredProducts = emptyList(),
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
        data class UnselectedCategoriesSet(val categories: List<Category>) : Msg
        data class FilteredProductsSet(val products: List<Product>) : Msg
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
                        addProductToBasketUseCase(intent.product)
                    }
                }

                HomeStore.Intent.OnClickBasket -> {
                    publish(HomeStore.Label.OnClickBasket)
                }

                is HomeStore.Intent.OnClickUnselectedCategory -> {
                    scope.launch {
                        async { selectCategoryUseCase(intent.category) }.await()
                    }
                }

                is HomeStore.Intent.OnClickSelectedCategory -> {
                    scope.launch {
                        async { unselectCategoryUseCase(intent.category.id) }.await()
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
                val filteredProducts = msg.products.filterByCategory(selectedCategories)
                copy(
                    allProducts = msg.products,
                    filteredProducts = filteredProducts
                )
            }

            is Msg.SelectedCategoriesLoaded -> {
                val selectedCategories = msg.categories
                val unselectedCategories =
                    allCategories.filterBySelectedCategories(selectedCategories)
                val filteredProducts = allProducts.filterByCategory(selectedCategories)
                copy(
                    selectedCategories = selectedCategories,
                    unselectedCategories = unselectedCategories,
                    filteredProducts = filteredProducts
                )
            }

            is Msg.AllCategoriesLoaded -> {
                val unselectedCategories =
                    msg.categories.filterBySelectedCategories(selectedCategories)
                copy(
                    allCategories = msg.categories,
                    unselectedCategories = unselectedCategories
                )
            }

            is Msg.FilteredProductsSet -> {
                copy(filteredProducts = msg.products)
            }

            is Msg.UnselectedCategoriesSet -> {
                copy(unselectedCategories = msg.categories)
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
                getSelectedCategoryUseCase().collect { selectedCats ->
                    dispatch(Action.SelectedCategoriesLoaded(selectedCats))
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

