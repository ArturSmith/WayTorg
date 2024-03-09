package com.way_torg.myapplication.presentation.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.way_torg.myapplication.R
import com.way_torg.myapplication.domain.entity.Product

@Composable
fun priceAnnotatedString(product: Product): AnnotatedString {
    val discount = product.price * product.discount / 100
    return buildAnnotatedString {
        if (discount > 0.0) {
            withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                append(text = "${product.price}")
            }
            withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.ExtraBold)) {
                append(text = " ${product.price - discount}")
            }
        } else {
            append(text = product.price.toString())
        }
    }
}

@Composable
fun quantityString(product: Product) = buildAnnotatedString {
    append(text = "${stringResource(R.string.quantity)}: ")
    append(text = "${product.count} ")
    if (product.count <= 5) {
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = Color.Red
            )
        ) {
            append(text = stringResource(R.string.almost_sold_out))
        }
    }
}


