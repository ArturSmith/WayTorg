package com.way_torg.myapplication.presentation.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.way_torg.myapplication.R
import com.way_torg.myapplication.presentation.ui.elements.ProductItem
import com.way_torg.myapplication.presentation.utils.priceAnnotatedString
import com.way_torg.myapplication.presentation.utils.quantityString

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(component: DetailsComponent) {

    val model by component.model.collectAsState()
    val localConfig = LocalConfiguration.current
    val halfOfScreenHeight = localConfig.screenHeightDp / 2
    val pagerState =
        rememberPagerState(pageCount = { model.product.pictures.size }, initialPage = 0)
    var descriptionExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton({ component.onClickBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
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
                    Spacer(Modifier.width(20.dp))
                },
                title = {
                    Text(text = model.product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Button(
                onClick = {
                    component.onClickAddToBasket(model.product)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            ) {
                Text(text = stringResource(R.string.add_to_basket))
            }
        },
        containerColor = Color.White
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(it).fillMaxSize()
        ) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(halfOfScreenHeight.dp)
                        .background(color = Color.White),
                ) {
                    if (model.product.pictures.isNotEmpty()) {
                        HorizontalPager(
                            state = pagerState,
                        ) { page ->
                            val pictures = model.product.pictures.values.toList()
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .padding(3.dp)
                                    .fillMaxSize(),
                                model = pictures[page],
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                loading = {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            )
                        }
                    } else {
                        Icon(Icons.Default.Image, contentDescription = null)
                    }
                    if (model.product.discount > 0.0) {
                        DiscountSpace(model.product.discount)
                    }
                    if (model.isAdmin) {
                        IconButton(
                            onClick = {
                                component.onClickEditProduct()
                            },
                            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        }
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 10.dp)
            }
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    shape = RoundedCornerShape(0),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            Text("${stringResource(R.string.price)}: ")
                            Text(text = priceAnnotatedString(model.product))
                        }
                        Text(text = quantityString(model.product))
                        Text(
                            text = model.product.description,
                            maxLines = if (descriptionExpanded) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = if (descriptionExpanded) stringResource(R.string.less) else stringResource(
                                R.string.more
                            ),
                            modifier = Modifier.fillMaxWidth().clickable {
                                descriptionExpanded = !descriptionExpanded

                            },
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(Modifier.height(20.dp))
            }

            items(
                items = model.products
            ) { product ->
                ProductItem(
                    product = product,
                    isInBasket = false,
                    modifier = Modifier.padding(5.dp),
                    onClickProduct = { component.onClickProduct(product) },
                    onClickAddToBasket = {}
                )
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}


@Composable
private fun DiscountSpace(discount: Double) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val trianglePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(400f, 0f)
            lineTo(0f, 400f)
            close()
        }
        drawPath(
            path = trianglePath,
            color = Color.Red
        )
    }

    Text(
        text = "${discount.toInt()}%",
        modifier = Modifier.rotate(-45f).offset(x = (-5).dp, y = 25.dp),
        color = Color.White,
        fontSize = 30.sp,
        fontWeight = FontWeight.ExtraBold
    )
}