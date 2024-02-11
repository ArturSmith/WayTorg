package com.way_torg.myapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.way_torg.myapplication.presentation.root.DefaultRootComponent
import com.way_torg.myapplication.presentation.root.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootContent(
                component = DefaultRootComponent(defaultComponentContext())
            )
        }
    }
}

