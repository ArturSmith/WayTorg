package com.way_torg.myapplication.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.AppName)) },
                actions = {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(text = model.productsInBasket.toString())
                            }
                        },
                        modifier = Modifier.clickable { component.onClickBasket() }
                    ) {
                        Icon(Icons.Filled.ShoppingBasket, contentDescription = null)
                    }
                    Spacer(Modifier.width(5.dp))
                    IconButton(onClick = { component.onClickChat() })
                    { Icon(Icons.Filled.Chat, contentDescription = null) }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onClickCreateProduct() }
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp)
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
                    ProductItem(
                        it,
                        modifier = Modifier,
                        onClickProduct = { component.onClickProduct(it) },
                        onClickAddToBasket = { component.onClickAddToBasket(it) }
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



