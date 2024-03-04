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
import com.way_torg.myapplication.domain.use_case.GetProductsFromBasketUseCase
import com.way_torg.myapplication.domain.use_case.SelectCategoryUseCase
import com.way_torg.myapplication.domain.use_case.SignInUseCase
import com.way_torg.myapplication.domain.use_case.SignOutUseCase
import com.way_torg.myapplication.domain.use_case.UnselectCategoryUseCase
import com.way_torg.myapplication.domain.use_case.getAuthStateUseCase
import com.way_torg.myapplication.extensions.convertToProductItems
import com.way_torg.myapplication.extensions.filterByCategory
import com.way_torg.myapplication.extensions.filterBySelectedCategories
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductToBasketUseCase: AddProductToBasketUseCase,
    private val getAllCategoriesFromRemoteDbUseCase: GetAllCategoriesFromRemoteDbUseCase,
    private val getSelectedCategoryUseCase: GetAllSelectedCategoriesFromLocalDbUseCase,
    private val selectCategoryUseCase: SelectCategoryUseCase,
    private val unselectCategoryUseCase: UnselectCategoryUseCase,
    private val getAuthStateUseCase: getAuthStateUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProductsFromBasketUseCase: GetProductsFromBasketUseCase
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
                    productsInBasket = emptyList(),
                    isContentVisible = false,
                    isAuthDialogVisible = false,
                    authState = false,
                    password = ""
                ),
                reducer = ReducerImpl,
                executorFactory = ::ExecutorImpl,
                bootstrapper = BootstrapperImpl()
            ) {}

    private sealed interface Action {
        data class ProductsLoaded(val products: List<Product>) : Action
        data class AllCategoriesLoaded(val categories: List<Category>) : Action
        data class SelectedCategoriesLoaded(val categories: List<Category>) : Action
        data class ProductsFromBasketLoaded(val products: List<String>) : Action
        data class AuthStateObserved(val authState: Boolean) : Action
    }

    private sealed interface Msg {

        data class SetAllProducts(val products: List<HomeStore.State.ProductItem>) : Msg
        data class AllCategoriesLoaded(val categories: List<Category>) : Msg
        data class SelectedCategoriesLoaded(val categories: List<Category>) : Msg
        data class UnselectedCategoriesSet(val categories: List<Category>) : Msg
        data class FilteredProductsSet(val products: List<HomeStore.State.ProductItem>) : Msg
        data class SetProductsFromBasket(val products: List<String>) : Msg
        data class SetVisibility(val value: Boolean) : Msg
        data class SetFilteredProducts(val products: List<HomeStore.State.ProductItem>) : Msg
        data object ChangeVisibilityOfAuthDialog : Msg
        data class SetAuthState(val authState: Boolean) : Msg
        data class SetPasswordValue(val value: String) : Msg

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
                    val filteredProducts = action.products
                        .convertToProductItems(state.productsInBasket)
                        .filterByCategory(state.selectedCategories)

                    dispatch(Msg.SetAllProducts(action.products.convertToProductItems(state.productsInBasket)))
                    dispatch(Msg.SetFilteredProducts(filteredProducts))

                    scope.launch {
                        delay(500)
                        dispatch(Msg.SetVisibility(true))
                    }
                }

                is Action.ProductsFromBasketLoaded -> {
                    dispatch(Msg.SetProductsFromBasket(action.products))
                }

                is Action.AuthStateObserved -> {
                    dispatch(Msg.SetAuthState(action.authState))
                }
            }
        }

        override fun executeIntent(intent: HomeStore.Intent, getState: () -> HomeStore.State) {
            val state = getState()
            when (intent) {
                is HomeStore.Intent.OnClickAddToBasket -> {
                    scope.launch {
                        addProductToBasketUseCase(intent.product)
                    }
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

                HomeStore.Intent.OnClickAuthButton -> {
                    if (state.authState) {
                        scope.launch {
                            signOutUseCase()
                        }
                    } else {
                        dispatch(Msg.ChangeVisibilityOfAuthDialog)
                    }
                }

                HomeStore.Intent.ChangeAuthDialogVisibility -> {
                    dispatch(Msg.ChangeVisibilityOfAuthDialog)
                }

                HomeStore.Intent.OnClickLogin -> {
                    scope.launch {
                        if (state.password.isNotEmpty()) {
                            val result = async { signInUseCase(state.password) }.await()
                            if (result.getOrNull() == true) {
                                dispatch(Msg.ChangeVisibilityOfAuthDialog)
                            }
                        }
                    }
                }

                is HomeStore.Intent.OnPasswordValueChangeListener -> {
                    dispatch(Msg.SetPasswordValue(intent.value))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<HomeStore.State, Msg> {
        override fun HomeStore.State.reduce(msg: Msg): HomeStore.State = when (msg) {
            is Msg.SetProductsFromBasket -> {
                copy(productsInBasket = msg.products)
            }

            is Msg.SetAllProducts -> {
                copy(allProducts = msg.products)
            }

            is Msg.SetFilteredProducts -> {
                copy(filteredProducts = msg.products)
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

            is Msg.SetVisibility -> {
                copy(isContentVisible = msg.value)
            }

            Msg.ChangeVisibilityOfAuthDialog -> {
                copy(isAuthDialogVisible = !isAuthDialogVisible)
            }

            is Msg.SetAuthState -> {
                copy(
                    authState = msg.authState
                )
            }

            is Msg.SetPasswordValue -> {
                copy(
                    password = msg.value
                )
            }
        }
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getProductsFromBasketUseCase().collect {
                    dispatch(Action.ProductsFromBasketLoaded(it))

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
            scope.launch {
                getAuthStateUseCase().collect {
                    dispatch(Action.AuthStateObserved(it))
                }
            }
        }
    }
}

