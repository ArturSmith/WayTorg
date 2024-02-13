package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Store
import com.way_torg.myapplication.domain.entity.Category

interface CreateProductStore :
    Store<CreateProductStore.Intent, CreateProductStore.State, CreateProductStore.Label> {

    data class State(
        val name: String,
        val category: Category,
        val description: String,
        val count: String,
        val price: String,
        val discount: String,
        val pictures: List<Uri>
    )

    sealed interface Label {
        data object OnClickBack : Label
        data object OnClickCreate : Label
    }


    sealed interface Intent {
        data class OnSetName(val name: String) : Intent
        data class OnSetCategory(val category: Category) : Intent
        data class OnSetCount(val count: String) : Intent
        data class OnSetDescription(val description: String) : Intent
        data class OnSetPrice(val price: String) : Intent
        data class OnSetDiscount(val discount: String) : Intent
        data class OnClickAddPictures(val pictures: List<Uri>) : Intent
        data class OnLongClickToPicture(val picture: Uri) : Intent
        data object OnClickBack : Intent
        data object OnClickCreate : Intent
    }
}