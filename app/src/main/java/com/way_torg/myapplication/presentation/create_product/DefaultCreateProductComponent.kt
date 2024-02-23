package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultCreateProductComponent @AssistedInject constructor(
    private val storeFactory: CreateProductStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onProductSaved") private val onProductSaved: () -> Unit,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
) : CreateProductComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CreateProductStore.State> = store.stateFlow

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    CreateProductStore.Label.OnProductCreated -> {
                        onProductSaved.invoke()
                    }
                }
            }
        }
    }

    override fun onSetName(name: String) {
        store.accept(CreateProductStore.Intent.OnSetName(name))
    }

    override fun onCategorySelected(category: Category) {
        store.accept(CreateProductStore.Intent.OnCategorySelected(category))
    }

    override fun onSetCount(count: String) {
        store.accept(CreateProductStore.Intent.OnSetCount(count))
    }

    override fun onSetDescription(description: String) {
        store.accept(CreateProductStore.Intent.OnSetDescription(description))
    }

    override fun onSetPrice(price: String) {
        store.accept(CreateProductStore.Intent.OnSetPrice(price))
    }

    override fun onSetDiscount(discount: String) {
        store.accept(CreateProductStore.Intent.OnSetDiscount(discount))
    }

    override fun onClickAddPictures(pictures: List<Uri>) {
        store.accept(CreateProductStore.Intent.OnClickAddPictures(pictures))
    }

    override fun onClickBack() {
        onClickBack.invoke()
    }

    override fun onClickCreate() {
        store.accept(CreateProductStore.Intent.OnClickCreate)
    }

    override fun onLongClickToPicture(picture: Uri) {
        store.accept(CreateProductStore.Intent.OnLongClickToPicture(picture))
    }

    override fun onSetNewCategory(text: String) {
        store.accept(CreateProductStore.Intent.OnSetNewCategory(text))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onProductSaved") onProductSaved: () -> Unit,
            @Assisted("onClickBack") onClickBack: () -> Unit,
        ): DefaultCreateProductComponent
    }
}