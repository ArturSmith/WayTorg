package com.way_torg.myapplication.presentation.basket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.extensions.ifNotEmpty
import com.way_torg.myapplication.presentation.utils.priceAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketContent(
    component: BasketComponent
) {
    val model by component.model.collectAsState()
    when (val state = model) {
        is BasketStore.State.Initial -> {
            InitialState(state, component)
        }

        BasketStore.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp))
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InitialState(model: BasketStore.State.Initial, component: BasketComponent) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.basket)) },
                navigationIcon = {
                    IconButton({ component.onClickBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Button(
                onClick = {
                    component.showSheet()
                },
                enabled = model.isOrderingEnable,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            ) {
                Text(text = stringResource(R.string.order))
            }
        }
    ) {
        if (model.products.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(
                    items = model.products,
                    key = { it.product.id }
                ) {
                    BasketProductItem(it, component)
                }
            }

            if (model.isSheetVisible) {
                BottomSheet(model) {
                    component.hideSheet()
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.list_is_empty))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasketProductItem(
    productWrapper: ProductWrapper,
    component: BasketComponent
) {
    val swipeState =
        rememberSwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
            confirmValueChange = {
                when (it) {
                    SwipeToDismissBoxValue.EndToStart -> {
                        component.delete(productWrapper.product.id)
                        true
                    }

                    SwipeToDismissBoxValue.StartToEnd -> true
                    SwipeToDismissBoxValue.Settled -> true
                }
            })
    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Red),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp).size(30.dp)
                )
            }
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color.White)
                    .clickable {
                        component.onClickProduct(productWrapper.product)
                    },
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    shape = RoundedCornerShape(3.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    productWrapper.product.pictures.ifNotEmpty(
                        ifNot = {
                            SubcomposeAsyncImage(
                                model = it.first(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        },
                        ifYes = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null)
                            }
                        }
                    )
                }
                Column(
                    modifier = Modifier.weight(2f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(text = productWrapper.product.name)
                    Row {
                        Text("${stringResource(R.string.price)}: ")
                        Text(text = priceAnnotatedString(productWrapper.product))
                    }
                    Text(text = "${stringResource(R.string.total_price)}: ${productWrapper.getTotalPriceWithDiscount()}")
                    Spacer(Modifier.weight(1f))
                    IncreaseDecreaseButtons(
                        modifier = Modifier.align(Alignment.Start),
                        quantity = productWrapper.quantityInBasket,
                        increase = { component.increaseQuantity(productWrapper.product.id) },
                        decrease = { component.decreaseQuantity(productWrapper.product.id) }
                    )
                }
            }
        }
    )


}


@Composable
private fun IncreaseDecreaseButtons(
    modifier: Modifier,
    quantity: Int,
    increase: () -> Unit,
    decrease: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "-",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            modifier = Modifier.clickable { decrease() })
        Text(
            text = quantity.toString(),
            fontSize = 20.sp,
        )
        Text(
            text = "+",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.clickable { increase() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    model: BasketStore.State.Initial,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn {
                itemsIndexed(items = model.products) { index, item ->
                    OrderProductItem(item, index + 1)
                }
                item {
                    Text(
                        text = annotatedTotalPriceString(model),
                        fontWeight = FontWeight.Bold
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = model.customerInfo.name,
                            placeholder = { Text(stringResource(R.string.name)) },
                            onValueChange = {}
                        )
                        OutlinedTextField(
                            value = model.customerInfo.address,
                            placeholder = { Text(stringResource(R.string.address)) },
                            onValueChange = {}
                        )
                        Text(
                            text = stringResource(R.string.contact_warning),
                            modifier = Modifier.width(TextFieldDefaults.MinWidth),
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                        OutlinedTextField(
                            value = model.customerInfo.contact,
                            placeholder = { Text(stringResource(R.string.contact)) },
                            onValueChange = {}
                        )
                        OutlinedTextField(
                            modifier = Modifier.height(100.dp),
                            value = model.customerInfo.message,
                            placeholder = { Text(stringResource(R.string.message)) },
                            onValueChange = {}
                        )
                        Button({}) {
                            Text(stringResource(R.string.order))
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun OrderProductItem(
    productWrapper: ProductWrapper,
    number: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = "$number. ${productWrapper.product.name} /")
        Text(text = "${productWrapper.quantityInBasket} /")
        Text(
            text = "${productWrapper.getTotalPriceWithoutDiscount()}",
            textDecoration = if (productWrapper.getDiscount() > 0.0) TextDecoration.LineThrough else TextDecoration.None
        )
        if (productWrapper.getDiscount() > 0.0) {
            Text(text = "${productWrapper.getTotalPriceWithDiscount()}", color = Color.Red)
        }
    }
}

@Composable
private fun annotatedTotalPriceString(model: BasketStore.State.Initial) =
    buildAnnotatedString {
        append("${stringResource(R.string.total_price)}:")
        append(" ${model.totalPriceWithoutDiscount}")
        if (model.getDiscount > 0.0) {
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(" - ${model.getDiscount}")
            }
            withStyle(style = SpanStyle()) {
                append(" = ${model.totalPriceWithDiscount}")
            }
        }
    }