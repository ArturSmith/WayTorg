package com.way_torg.myapplication.presentation.create_product

import android.net.Uri
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.way_torg.myapplication.R
import com.way_torg.myapplication.extensions.asInitial

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductContent(
    component: CreateProductComponent
) {
    val model by component.model.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.create_product)) },
                navigationIcon = {
                    IconButton(onClick = {
                        component.onClickBack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it).fillMaxSize(),
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InitialState(
    state: CreateProductStore.State.Initial,
    component: CreateProductComponent
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = state.name,
            placeholder = { Text(stringResource(R.string.name)) },
            onValueChange = {
                component.onSetName(it)
            }
        )
//                OutlinedTextField(
//                    value = state.category,
//                    placeholder = { Text(stringResource(R.string.category)) },
//                    onValueChange = {
//                        component.onSetCategory()
//                    }
//                )
        OutlinedTextField(
            value = state.description,
            placeholder = { Text(stringResource(R.string.description)) },
            onValueChange = {
                component.onSetDescription(it)
            },
            modifier = Modifier.height(100.dp).widthIn(max = TextFieldDefaults.MinWidth)
        )
        OutlinedTextField(
            value = state.count,
            placeholder = { Text(stringResource(R.string.count)) },
            onValueChange = {
                component.onSetCount(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = state.price,
            placeholder = { Text(stringResource(R.string.price)) },
            onValueChange = {
                component.onSetPrice(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = state.discount,
            placeholder = { Text(stringResource(R.string.discount)) },
            onValueChange = {
                component.onSetDiscount(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        if (state.pictures.isNotEmpty()) {
            Text(
                stringResource(R.string.long_click_to_delete_pic),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Gray
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
                stringResource(R.string.no_selected_pictures),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
        TakePicturesFromGalery {
            component.onClickAddPictures(it)
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = {
                component.onClickCreate()
            },
        ) {
            Text(stringResource(R.string.create), color = Color.Black)
        }
        Spacer(modifier = Modifier.height(10.dp))
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