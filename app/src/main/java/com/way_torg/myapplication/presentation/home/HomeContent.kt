package com.way_torg.myapplication.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.entity.Product

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
                    IconButton(onClick = { component.onClickBasket() })
                    { Icon(Icons.Filled.ShoppingBasket, contentDescription = null) }
                    IconButton(onClick = { component.onClickChat() })
                    { Icon(Icons.Filled.Chat, contentDescription = null) }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onClickCreateProduct() }
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
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
                    items = model.products
                ) {
                    ProductCard(
                        it,
                        onClickProduct = { component.onClickProduct(it) },
                        onClickAddToBasket = { component.onClickAddToBasket(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onClickProduct: () -> Unit,
    onClickAddToBasket: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(color = Color.White),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
                .clickable { onClickProduct() },
            shape = RoundedCornerShape(10.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = product.pictures) {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Text(text = product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = "${stringResource(R.string.rating)} - ${product.rating}")
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(product.price.toString())
            }
            if (product.discount > 0.0) {
                withStyle(style = SpanStyle(color = Color.Red, fontSize = 20.sp)) {
                    append("- ${product.discount}%")
                }
            }
        }
        Text(text = "${stringResource(R.string.left_in_stock)} - ${product.count}")
        OutlinedButton(
            onClick = { onClickAddToBasket() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.add_to_basket), color = Color.Black)
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


