package com.way_torg.myapplication.presentation.ui.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.way_torg.myapplication.domain.entity.Product
import com.way_torg.myapplication.extensions.ifNotEmpty
import com.way_torg.myapplication.presentation.utils.priceAnnotatedString

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier,
    onClickProduct: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        /**
         *  Product first picture
         */
        PictureHeader(product) {
            onClickProduct()
        }
        /**
         *  Product name
         */
        Text(
            text = product.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        /**
         *  Product price
         */
        Text(
            text = priceAnnotatedString(product, redPrice = false),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray

        )
    }
}


@Composable
private fun PictureHeader(product: Product, onClickProduct: () -> Unit) {
    val discount = product.price * product.discount / 100
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClickProduct() },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(Modifier.fillMaxSize()) {
            product.pictures.ifNotEmpty {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .align(Alignment.Center),
                    model = product.pictures.values.first(),
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

