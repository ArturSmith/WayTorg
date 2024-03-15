package com.way_torg.myapplication.presentation.logo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.way_torg.myapplication.R
import com.way_torg.myapplication.presentation.ui.theme.MainOrange
import kotlinx.coroutines.delay

@Composable
fun LogoContent(component: LogoComponent) {

    var scaleState by remember { mutableIntStateOf(0) }
    val targetValue = when (scaleState) {
        1 -> 1f
        2 -> 100f
        else -> 0f
    }
    val scale =
        animateFloatAsState(
            targetValue = targetValue,
            animationSpec = tween(durationMillis = 1000),
            label = ""
        )
    LaunchedEffect(Unit) {
        scaleState = 1
        delay(1500)
        scaleState = 2
        delay(1000)
        component.navigateToHomeScreen()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.AppName),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MainOrange,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        )


        Image(
            painterResource(R.drawable.waytorglogo),
            contentDescription = null,
            modifier = Modifier.scale(scale.value)
        )
    }
}