package com.way_torg.myapplication.presentation.order

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Order
import com.way_torg.myapplication.domain.entity.OrderStatus
import com.way_torg.myapplication.domain.entity.ProductWrapper
import com.way_torg.myapplication.domain.entity.OrderStringHandler
import com.way_torg.myapplication.domain.entity.ProductStringHandler
import com.way_torg.myapplication.extensions.convertToDataFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatContent(
    component: OrdersComponent
) {
    val model by component.model.collectAsState()
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton({
                        component.onClickBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(R.string.orders))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(it)
        ) {
            val (tabs, content) = createRefs()
            TabRow(
                selectedTabIndex = model.selectedTab.index,
                modifier = Modifier.constrainAs(tabs) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                containerColor = Color.White
            ) {
                model.tabs.forEachIndexed { index, status ->
                    val isSelected = index == model.selectedTab.index
                    val text = when (status) {
                        OrderStatus.UNPAID -> stringResource(R.string.unpaid)
                        OrderStatus.PAID -> stringResource(R.string.paid)
                        OrderStatus.CANCELED -> stringResource(R.string.canceled)
                        OrderStatus.DELAYED -> stringResource(R.string.delayed)
                    }
                    Tab(
                        selected = isSelected,
                        onClick = {
                            component.onClickTab(status)
                        },
                        text = {
                            Text(
                                text = text.uppercase(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().constrainAs(content) {
                    top.linkTo(tabs.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                itemsIndexed(
                    items = model.selectedOrders
                ) { _, item ->
                    OrderItem(
                        order = item,
                        onClick = { component.onClickOrder(it) },
                        delete = { component.deleteOrder(it) })
                }
            }
            if (model.showModalSheet != null) {
                OrderDetailsModalSheet(
                    order = model.showModalSheet as Order,
                    isEditingEnable = model.isProductEditingEnable,
                    component = component
                )
            }
        }
    }
}

@Composable
private fun OrderItem(
    order: Order,
    onClick: (order: Order) -> Unit,
    delete: (order: Order) -> Unit
) {
    val stateItemColor = when (order.status) {
        OrderStatus.UNPAID -> Color.Red
        OrderStatus.PAID -> Color.Green
        OrderStatus.CANCELED -> Color.Gray
        OrderStatus.DELAYED -> Color.Gray
    }

    var confirmDeletingDialog by remember { mutableStateOf(false) }

    if (confirmDeletingDialog) {
        Dialog(
            onDismissRequest = {
                confirmDeletingDialog = false
            }
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                TextButton(
                    modifier = Modifier.padding(20.dp),
                    onClick = {
                        delete(order)
                    }) {
                    Text(stringResource(R.string.confirm_deleting), color = Color.Red)
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClick(order)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (mainInfo, deleteButton) = createRefs()

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .constrainAs(mainInfo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(text = order.orderDate.convertToDataFormat(), color = Color.Gray)
                Text(text = order.customerInfo.name)
                Text(
                    text = order.customerInfo.address,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                StatusItem(order.status, stateItemColor)
            }

            TextButton(
                modifier = Modifier
                    .constrainAs(deleteButton) {
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                        end.linkTo(parent.end, margin = 5.dp)
                    },
                onClick = {
                    confirmDeletingDialog = !confirmDeletingDialog
                }
            ) {
                Text(stringResource(R.string.delete_order), color = Color.Red)
            }
        }

    }
}

@Composable
private fun StatusItem(status: OrderStatus, color: Color) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.name,
            color = Color.White,
            modifier = Modifier.padding(5.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderDetailsModalSheet(
    order: Order,
    isEditingEnable: Boolean,
    component: OrdersComponent
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val orderStringHandler = OrderStringHandler(order)
    ModalBottomSheet(
        onDismissRequest = { component.closeModalSheet() },
        sheetState = sheetState,
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(text = stringResource(R.string.items), fontWeight = FontWeight.Bold)
            }
            items(
                items = order.products
            ) { item ->
                ProductItem(
                    item = item,
                    isEditingEnable = isEditingEnable,
                    increase = {
                        component.onClickIncreaseQuantity(order, item)
                    },
                    decrease = {
                        component.onClickDecreaseQuantity(order, item)
                    },
                    delete = {
                        component.onClickDeleteProduct(order, item)
                    }
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.total_quantity_of_products)}: ${order.totalQuantityOfProducts()}",
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.total_price)}: ${orderStringHandler.strTotalPriceWithDiscount}",
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.total_discount)}: ${orderStringHandler.strTotalDiscount} (${
                        order.averageDiscountInPercent().toInt()
                    }%)",
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.customer)}: ${order.customerInfo.name}",
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.address)}: ${order.customerInfo.address}",
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = "${stringResource(R.string.contact)}: ${order.customerInfo.contact}",
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                StatusRow(order.status) {
                    component.onClickStatus(order, it)
                }
            }
            item {
                Spacer(Modifier.height(50.dp))
            }
        }
    }
}


@Composable
private fun ProductItem(
    item: ProductWrapper,
    isEditingEnable: Boolean,
    increase: () -> Unit,
    decrease: () -> Unit,
    delete: () -> Unit
) {
    val stringHandler = ProductStringHandler(item)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f)
            ) {
                Text(text = "${stringResource(R.string.serial_number)}: ${item.product.serialNumber}")
                Text(text = item.product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "${stringResource(R.string.quantity)}: ${item.quantity}")
                Spacer(Modifier)
                Row {
                    Text(text = "${stringResource(R.string.total_price)}: ")
                    Text(text = buildAnnotatedString {
                        if (item.getDiscount() > 0.0) {
                            append(stringHandler.strTotalPriceWithoutDiscount)
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append(" - ")
                                append(stringHandler.strTotalDiscount)
                            }
                            append(" = ")
                            append(stringHandler.strTotalPrice)
                        } else {
                            append(stringHandler.strTotalPriceWithoutDiscount)
                        }
                    })
                }
            }
            if (isEditingEnable) {
                Column(
                    modifier = Modifier
                ) {
                    IconButton({
                        increase()
                    }) {
                        Text(text = "+1", fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    }
                    IconButton({
                        decrease()
                    }) {
                        Text(text = "-1", fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    }
                    IconButton({
                        delete()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusRow(currentStatus: OrderStatus, onClick: (status: OrderStatus) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OrderStatus.entries.forEach {
            TextButton(
                onClick = { onClick(it) },
            ) {
                Text(
                    text = it.name,
                    modifier = Modifier,
                    color = if (it == currentStatus) MaterialTheme.colorScheme.primary else Color.Black
                )
            }
        }
    }
}



