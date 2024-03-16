package com.way_torg.myapplication.presentation.logo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Shop2
import androidx.compose.material.icons.filled.ShopTwo
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.way_torg.myapplication.presentation.ui.theme.MainOrange
import com.way_torg.myapplication.presentation.ui.theme.Pink40
import com.way_torg.myapplication.presentation.ui.theme.NewBlue
import com.way_torg.myapplication.presentation.ui.theme.Pink
import kotlinx.coroutines.delay

@Composable
fun LogoContent(component: LogoComponent) {

    var targetValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        repeat(2){
            targetValue = 1f
            delay(1250)
            targetValue = 0f
            delay(1250)
        }
        component.navigateToHomeScreen()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Cube(NewBlue, Icons.Default.ShoppingBag, targetValue, 0)
                Cube(MainOrange, Icons.Default.ShoppingCart, targetValue, 250)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Cube(Pink, Icons.Default.ShoppingBasket, targetValue, 750)
                Cube(Color.Green, Icons.Default.Storefront, targetValue, 500)
            }
        }

    }
}


@Composable
private fun Cube(color: Color, icon: ImageVector, targetValue: Float, delay: Int) {
    val scale by
    animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 500, delay),
        label = ""
    )
    Card(
        modifier = Modifier
            .size(100.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}