package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
import com.way_torg.myapplication.domain.entity.Category
import kotlinx.coroutines.flow.StateFlow

interface CreateProductComponent {
    val model: StateFlow<CreateProductStore.State>

    fun onSetName(name:String)
    fun onSetCategory(category: Category)
    fun onSetCount(count:String)
    fun onSetDescription(description:String)
    fun onSetPrice(price:String)
    fun onSetDiscount(discount:String)
    fun onClickAddPictures(pictures:List<Uri>)
    fun onLongClickToPicture(picture:Uri)
    fun onClickBack()
    fun onClickCreate()
}