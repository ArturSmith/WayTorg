package com.way_torg.myapplication.presentation.create_product

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.domain.use_case.CreateCategoryUseCase
import com.way_torg.myapplication.domain.use_case.CreateProductUseCase
import com.way_torg.myapplication.domain.use_case.GetAllCategoriesFromRemoteDbUseCase
import com.way_torg.myapplication.domain.use_case.UnselectCategoryUseCase
import com.way_torg.myapplication.extensions.asInitial
import com.way_torg.myapplication.extensions.getOrCreateCategory
import com.way_torg.myapplication.extensions.toNewDouble
import com.way_torg.myapplication.extensions.toNewInt
import com.way_torg.myapplication.presentation.home.HomeStoreFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class CreateProductStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val context: Context,
    private val createProductUseCase: CreateProductUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getAllCategoriesFromRemoteDbUseCase: GetAllCategoriesFromRemoteDbUseCase
) {
    fun create(): CreateProductStore = object : CreateProductStore,
        Store<CreateProductStore.Intent, CreateProductStore.State, CreateProductStore.Label>
        by storeFactory.create(
            name = "CreateProductStore",
            initialState = CreateProductStore.State.Initial(
                name = "",
                selectedCategory = Category("", ""),
                categoryName = context.resources.getString(R.string.category),
                description = "",
                count = "",
                price = "",
                discount = "",
                pictures = emptyList(),
                allCategories = emptyList()
            ),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl,
            bootstrapper = BootstrapperImpl()
        ) {}


    private sealed interface Action {
        data class CategoriesLoaded(val categories: List<Category>) : Action
    }


    private sealed interface Msg {

        data class OnSetName(val name: String) : Msg
        data class OnCategorySelected(val category: Category) : Msg
        data class OnSetCount(val count: String) : Msg
        data class OnSetDescription(val description: String) : Msg
        data class OnSetPrice(val price: String) : Msg
        data class OnSetDiscount(val discount: String) : Msg
        data class OnClickAddPictures(val pictures: List<Uri>) : Msg
        data class OnLongClickToPicture(val picture: Uri) : Msg
        data class OnSetNewCategory(val text: String) : Msg
        data class CategoriesLoaded(val categories: List<Category>) : Msg

        data object Loading : Msg
        data object Error : Msg
        data object Success : Msg

    }

    private inner class ExecutorImpl :
        CoroutineExecutor<CreateProductStore.Intent, Action, CreateProductStore.State, Msg, CreateProductStore.Label>() {
        override fun executeAction(action: Action, getState: () -> CreateProductStore.State) {
            when (action) {
                is Action.CategoriesLoaded -> {
                    dispatch(Msg.CategoriesLoaded(action.categories))
                }
            }
        }

        override fun executeIntent(
            intent: CreateProductStore.Intent,
            getState: () -> CreateProductStore.State
        ) {
            when (intent) {
                is CreateProductStore.Intent.OnClickAddPictures -> {
                    dispatch(Msg.OnClickAddPictures(pictures = intent.pictures))
                }

                CreateProductStore.Intent.OnClickBack -> {
                    publish(CreateProductStore.Label.OnClickBack)
                }

                is CreateProductStore.Intent.OnClickCreate -> {
                    val state = getState().asInitial()
                    scope.launch {

                        dispatch(Msg.Loading)

                        val category = getCategory(state.allCategories, state.categoryName)

                        val product = Product(
                            id = UUID.randomUUID().toString(),
                            name = state.name.trim(),
                            category = category,
                            description = state.description.trim(),
                            count = state.count.toNewInt(),
                            price = state.price.toNewDouble(),
                            discount = state.discount.toNewDouble(),
                            pictures = emptyList(),
                            rating = 0.0
                        )
                        val result = async { createProductUseCase(product, state.pictures) }.await()

                        if (result.isFailure) {
                            dispatch(Msg.Error)
                        } else {
                            dispatch(Msg.Success)
                            delay(500)
                            publish(CreateProductStore.Label.OnProductCreated)
                        }
                    }
                }

                is CreateProductStore.Intent.OnCategorySelected -> {
                    dispatch(Msg.OnCategorySelected(intent.category))
                }

                is CreateProductStore.Intent.OnSetCount -> {
                    dispatch(Msg.OnSetCount(intent.count))
                }

                is CreateProductStore.Intent.OnSetDescription -> {
                    dispatch(Msg.OnSetDescription(intent.description))
                }

                is CreateProductStore.Intent.OnSetDiscount -> {
                    dispatch(Msg.OnSetDiscount(intent.discount))
                }

                is CreateProductStore.Intent.OnSetName -> {
                    dispatch(Msg.OnSetName(intent.name))
                }

                is CreateProductStore.Intent.OnSetPrice -> {
                    dispatch(Msg.OnSetPrice(intent.price))
                }

                is CreateProductStore.Intent.OnLongClickToPicture -> {
                    dispatch(Msg.OnLongClickToPicture(intent.picture))
                }

                is CreateProductStore.Intent.OnSetNewCategory -> {
                    dispatch(Msg.OnSetNewCategory(intent.categoryName))
                }
            }
        }


        private suspend fun getCategory(categories: List<Category>, newCategory: String) =
            withContext(Dispatchers.IO) {
                newCategory.getOrCreateCategory(categories) {
                    val category =
                        Category(id = UUID.randomUUID().toString(), name = it)
                    val result = async { createCategoryUseCase(category) }.await()
                    if (result.isSuccess) category else Category.defaultInstance
                }
            }
    }


    private object ReducerImpl : Reducer<CreateProductStore.State, Msg> {
        override fun CreateProductStore.State.reduce(msg: Msg): CreateProductStore.State =
            when (this) {
                is CreateProductStore.State.Initial -> reduceInitial(msg)
                CreateProductStore.State.Error,
                CreateProductStore.State.Loading,
                CreateProductStore.State.Success -> reduceOther(msg)
            }

        private fun CreateProductStore.State.Initial.reduceInitial(msg: Msg): CreateProductStore.State =
            when (msg) {
                is Msg.OnClickAddPictures -> {
                    copy(pictures = msg.pictures)
                }

                is Msg.OnCategorySelected -> {
                    copy(
                        selectedCategory = msg.category,
                        categoryName = msg.category.name
                    )
                }

                is Msg.OnSetCount -> {
                    copy(count = msg.count.trim())
                }

                is Msg.OnSetDescription -> {
                    copy(
                        description = msg.description
                    )
                }

                is Msg.OnSetDiscount -> {
                    copy(
                        discount = msg.discount.trim()
                    )
                }

                is Msg.OnSetName -> {
                    copy(
                        name = msg.name
                    )
                }

                is Msg.OnSetPrice -> {
                    copy(
                        price = msg.price.trim()
                    )
                }

                is Msg.OnLongClickToPicture -> {
                    val newList = pictures.toMutableList()
                    newList.remove(msg.picture)
                    copy(
                        pictures = newList
                    )
                }

                is Msg.OnSetNewCategory -> {
                    copy(categoryName = msg.text)
                }

                is Msg.CategoriesLoaded -> {
                    copy(allCategories = msg.categories)
                }

                Msg.Error,
                Msg.Loading,
                Msg.Success -> reduceOther(msg)

            }

        private fun CreateProductStore.State.reduceOther(msg: Msg): CreateProductStore.State =
            when (msg) {
                is Msg.Loading -> CreateProductStore.State.Loading
                is Msg.Error -> CreateProductStore.State.Error
                is Msg.Success -> CreateProductStore.State.Success
                else -> this
            }
    }

    private inner class BootstrapperImpl :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getAllCategoriesFromRemoteDbUseCase().collect {
                    dispatch(Action.CategoriesLoaded(it))
                }
            }
        }
    }

}