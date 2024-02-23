package com.way_torg.myapplication.presentation.ui.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.extensions.ifNotEmpty
import com.way_torg.myapplication.presentation.utils.priceAnnotatedString

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier,
    onClickProduct: () -> Unit,
    onClickAddToBasket: () -> Unit
) {
    val discount = product.price * product.discount / 100

    Column(
        modifier = modifier.fillMaxWidth().background(color = Color.White),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onClickProduct() },
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                product.pictures.ifNotEmpty {
                    SubcomposeAsyncImage(
                        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
                        model = product.pictures.first(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        error = {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Error, contentDescription = null)
                            }
                        }
                    )
                }
                if (discount > 0.0) {
                    DiscountSpace(product.discount, Modifier.size(60.dp))
                }
            }

        }
        Text(text = product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row {
            Text("${stringResource(R.string.price)}: ")
            Text(text = priceAnnotatedString(product))
        }
        Text(
            text = "${stringResource(R.string.quantity)}: ${product.count}",
            fontSize = 12.sp
        )
        OutlinedButton(
            onClick = { onClickAddToBasket() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.add_to_basket), color = Color.Black)
        }

    }
}
@Composable
private fun DiscountSpace(discount: Double, modifier: Modifier) {
    Canvas(modifier = modifier) {
        val trianglePath = Path().apply {
            moveTo(x = 0f, y = 0f)
            lineTo(x = size.width, y = 0f)
            lineTo(x = 0f, y = size.height)
            close()
        }
        drawPath(path = trianglePath, color = Color.Red)
    }
    Text(
        text = "${discount.toInt()}%",
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .padding(5.dp)
            .rotate(-45f)
    )
}

