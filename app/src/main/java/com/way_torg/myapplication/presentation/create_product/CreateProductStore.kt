package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Store
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product

interface CreateProductStore :
    Store<CreateProductStore.Intent, CreateProductStore.State, CreateProductStore.Label> {

    sealed interface State {
        data class Initial(
            val name: String,
            val isNameError: Boolean,
            val selectedCategory: Category?,
            val isCategoryError: Boolean,
            val categoryName: String,
            val description: String,
            val isDescriptionError: Boolean,
            val count: String,
            val isCountError: Boolean,
            val price: String,
            val isPriceError: Boolean,
            val discount: String,
            val pictures: List<Uri>,
            val allCategories: List<Category>,
            private val product: Product?
        ) : State {

            val isDeletingEnable: Boolean
                get() = product != null

            fun isAnyRequiredFieldEmpty() = name.isEmpty()
                    || price.isEmpty()
                    || description.isEmpty()
                    || count.isEmpty()
                    || categoryName.isEmpty()

            fun validateFields() = this.copy(
                isNameError = name.isEmpty(),
                isPriceError = price.isEmpty(),
                isDescriptionError = description.isEmpty(),
                isCountError = count.isEmpty(),
                isCategoryError = categoryName.isEmpty()
            )

            fun getProduct() = product
            fun isEditing() = product != null

        }

        data object Loading : State
        data object Error : State
        data object Success : State
    }


    sealed interface Label {
        data object OnNavigateBack : Label
    }


    sealed interface Intent {
        data class OnSetName(val name: String) : Intent
        data class OnCategorySelected(val category: Category) : Intent
        data class OnSetCount(val count: String) : Intent
        data class OnSetDescription(val description: String) : Intent
        data class OnSetPrice(val price: String) : Intent
        data class OnSetDiscount(val discount: String) : Intent
        data class OnSetNewCategory(val categoryName: String) : Intent
        data class OnClickAddPictures(val pictures: List<Uri>) : Intent
        data class OnLongClickToPicture(val picture: Uri) : Intent
        data object OnClickCreate : Intent
        data object OnClickDeleteProduct : Intent
    }
}