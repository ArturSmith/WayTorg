package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Category

@Composable
fun CreateProductContent(
    component: CreateProductComponent
) {
    val model by component.model.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = model) {
            is CreateProductStore.State.Initial -> {
                InitialState(state, component)
            }

            CreateProductStore.State.Error -> {
                ErrorState(state)
            }

            CreateProductStore.State.Loading -> {
                LoadingState(state)
            }

            CreateProductStore.State.Success -> {
                SuccessState(state)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun InitialState(
    state: CreateProductStore.State.Initial,
    component: CreateProductComponent
) {
    val title = if (state.isEditing()) R.string.edit else R.string.create_product
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(title)) },
                navigationIcon = {
                    IconButton(onClick = {
                        component.onClickBack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (state.isDeletingEnable) {
                        IconButton({
                            component.onClickDelete()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = state.name,
                label = { Text(stringResource(R.string.name)) },
                onValueChange = {
                    component.onSetName(it)
                },
                isError = state.isNameError,
                modifier = Modifier.widthIn(max = TextFieldDefaults.MinWidth),
            )
            OutlinedTextField(
                value = state.count,
                label = { Text(stringResource(R.string.count)) },
                onValueChange = {
                    component.onSetCount(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = state.isCountError
            )
            OutlinedTextField(
                value = state.price,
                label = { Text(stringResource(R.string.price)) },
                onValueChange = {
                    component.onSetPrice(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = state.isPriceError
            )
            OutlinedTextField(
                value = state.discount,
                label = { Text(stringResource(R.string.discount)) },
                onValueChange = {
                    component.onSetDiscount(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            if (state.isCategoryError) {
                Text(
                    text = stringResource(R.string.select_category),
                    color = Color.Red
                )
            }
            Categories(
                categories = state.allCategories,
                categoryName = state.categoryName,
                onSelected = { component.onCategorySelected(it) },
                onSetNewCategory = { component.onSetNewCategory(it) }
            )
            OutlinedTextField(
                value = state.description,
                label = { Text(stringResource(R.string.description)) },
                onValueChange = {
                    component.onSetDescription(it)
                },
                modifier = Modifier.height(100.dp).widthIn(max = TextFieldDefaults.MinWidth),
                isError = state.isDescriptionError
            )
            if (state.isEditing() && state.getProductPictures().isNotEmpty()) {
                Text(
                    "**${stringResource(R.string.you_can_only_delete_all_pictures)}",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(TextFieldDefaults.MinWidth)
                )
                TextButton(
                    onClick = {},
                    modifier = Modifier
                        .width(TextFieldDefaults.MinWidth)
                        .height(40.dp)
                        .clip(RoundedCornerShape(1.dp))
                ) {
                    Text(
                        text = stringResource(R.string.delete_all_pictures),
                        fontSize = 12.sp
                    )
                }
                ExistingPictures(state.getProductPictures())
            }
            if (state.pictures.isNotEmpty()) {
                Text(
                    stringResource(R.string.long_click_to_delete_pic),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(TextFieldDefaults.MinWidth)
                )
                LazyRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = state.pictures
                    ) {
                        AsyncImage(
                            model = it,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(200.dp).combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    component.onLongClickToPicture(it)
                                }
                            )
                        )
                    }
                }
            } else {
                Text(
                    stringResource(R.string.select_new_pictures),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
            if (state.isEditing()) {

            }
            TakePicturesFromGalery {
                component.onClickAddPictures(it)
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    component.onClickCreate()
                },
                modifier = Modifier.width(TextFieldDefaults.MinWidth)
            ) {
                Text(
                    if (state.isEditing()) stringResource(R.string.edit) else stringResource(R.string.create),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}

@Composable
private fun LoadingState(state: CreateProductStore.State) {
    CircularProgressIndicator()
}

@Composable
private fun ErrorState(state: CreateProductStore.State) {

}

@Composable
private fun SuccessState(state: CreateProductStore.State) {
    Icon(
        Icons.Filled.Done,
        contentDescription = null,
        tint = Color.Green,
        modifier = Modifier.size(50.dp)
    )
}

@Composable
private fun ExistingPictures(pictures: List<String>) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = pictures
        ) {
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
private fun TakePicturesFromGalery(onSetPictures: (pictures: List<Uri>) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) {
        onSetPictures(it)
    }
    Box(
        modifier = Modifier
            .border(
                border = BorderStroke(width = 1.dp, color = Color.Black),
                shape = RoundedCornerShape(4.dp)
            )
            .size(width = TextFieldDefaults.MinWidth, height = 100.dp)
            .clickable {
                launcher.launch(PickVisualMediaRequest())
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Image, contentDescription = null)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Categories(
    categories: List<Category>,
    categoryName: String,
    onSelected: (category: Category) -> Unit,
    onSetNewCategory: (text: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = expanded,
        onExpandedChange = {
            Log.d("Categories", expanded.toString())
            expanded = !expanded
        },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = categoryName,
            onValueChange = {
                onSetNewCategory(it)
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category.name) },
                    onClick = {
                        onSelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}