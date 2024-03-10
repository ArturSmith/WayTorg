package com.way_torg.myapplication.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.presentation.ui.elements.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    component: HomeComponent
) {
    val model by component.model.collectAsState()
    val context = LocalContext.current
    val mainAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    if (model.isAuthDialogVisible) {
        AuthDialog(
            value = model.password,
            onValueChange = { component.onPasswordValueChangeListener(it) },
            onClickLogin = { component.onClickLogin() }
        ) {
            component.changeAuthDialogVisibility()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if (model.authState) {
                        IconButton(
                            onClick = { component.onClickCreateProduct() }
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(text = model.countOfUnpaidOrders.toString())
                                }
                            },
                            modifier = Modifier
                                .padding(15.dp)
                                .clickable { component.onClickChat() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Chat,
                                contentDescription = null
                            )
                        }
                    }
                },
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { component.onClickAuthButton() }
                        ) {
                            Icon(
                                imageVector = if (model.authState) Icons.Filled.Logout else Icons.Filled.Login,
                                contentDescription = null
                            )
                        }
                        IconButton({
                            component.changeLocale(context)
                        }) {
                            Text(
                                model.currentLocale,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    ),
                scrollBehavior = mainAppBarScrollBehavior,
            )
        },
        containerColor = Color.White,
        modifier = Modifier.nestedScroll(mainAppBarScrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable { component.onClickBasket() },
                contentAlignment = Alignment.Center
            ) {
                BadgedBox(
                    badge = {
                        Badge {
                            Text(text = model.productsInBasket.size.toString())
                        }
                    },
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBasket,
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        if (model.filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.list_is_empty))
            }
        } else {
            Content(model, component, it)
        }
    }
}

@Composable
private fun Content(
    model: HomeStore.State,
    component: HomeComponent,
    paddingValues: PaddingValues,
) {
    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = model.unselectedCategories
            ) {
                CategoryCard(it, false) {
                    component.onClickUnselectedCategory(it)
                }
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(
                items = model.selectedCategories
            ) {
                CategoryCard(it, true) {
                    component.onClickSelectedCategory(it)
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = model.filteredProducts,
            ) {
                AnimatedVisibility(model.isContentVisible) {
                    ProductItem(
                        it.product,
                        modifier = Modifier,
                        onClickProduct = { component.onClickProduct(it.product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: Category, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = { onClick() },
        label = {
            Text(category.name)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthDialog(
    value: String,
    onValueChange: (string: String) -> Unit,
    onClickLogin: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val height = (LocalConfiguration.current.screenHeightDp / 2).dp
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.height(height)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.enter_password))
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    modifier = Modifier.padding(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(10.dp))
                TextButton({
                    onClickLogin()
                }) {
                    Text(stringResource(R.string.login))
                }
            }
        }
    }
}



