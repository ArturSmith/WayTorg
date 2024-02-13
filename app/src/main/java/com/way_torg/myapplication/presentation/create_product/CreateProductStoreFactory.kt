package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.way_torg.myapplication.domain.entity.Category
import javax.inject.Inject

class CreateProductStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {
    fun create(): CreateProductStore = object : CreateProductStore,
        Store<CreateProductStore.Intent, CreateProductStore.State, CreateProductStore.Label>
        by storeFactory.create(
            name = "CreateProductStore",
            initialState = CreateProductStore.State(
                name = "",
                category = Category("", ""),
                description = "",
                count = "",
                price = "",
                discount = "",
                pictures = emptyList()
            ),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl
        ) {}


    private sealed interface Action


    private sealed interface Msg {
        data class OnSetName(val name: String) : Msg
        data class OnSetCategory(val category: Category) : Msg
        data class OnSetCount(val count: String) : Msg
        data class OnSetDescription(val description: String) : Msg
        data class OnSetPrice(val price: String) : Msg
        data class OnSetDiscount(val discount: String) : Msg
        data class OnClickAddPictures(val pictures: List<Uri>) : Msg
        data class OnLongClickToPicture(val picture: Uri) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<CreateProductStore.Intent, Action, CreateProductStore.State, Msg, CreateProductStore.Label>() {
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

                CreateProductStore.Intent.OnClickCreate -> {
                    TODO("Call function from repository and wait for response")
                    publish(CreateProductStore.Label.OnClickCreate)
                }

                is CreateProductStore.Intent.OnSetCategory -> {
                    dispatch(Msg.OnSetCategory(intent.category))
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
            }
        }
    }

    private object ReducerImpl : Reducer<CreateProductStore.State, Msg> {
        override fun CreateProductStore.State.reduce(msg: Msg): CreateProductStore.State =
            when (msg) {
                is Msg.OnClickAddPictures -> {
                    copy(pictures = pictures + msg.pictures)
                }

                is Msg.OnSetCategory -> {
                    copy(category = msg.category)
                }

                is Msg.OnSetCount -> {
                    copy(count = msg.count)
                }

                is Msg.OnSetDescription -> {
                    copy(description = msg.description)
                }

                is Msg.OnSetDiscount -> {
                    copy(discount = msg.discount)
                }

                is Msg.OnSetName -> {
                    copy(name = msg.name)
                }

                is Msg.OnSetPrice -> {
                    copy(price = msg.price)
                }

                is Msg.OnLongClickToPicture -> {
                    val newList = pictures.toMutableList()
                    newList.remove(msg.picture)
                    copy(pictures = newList.toList())
                }
            }
    }
}