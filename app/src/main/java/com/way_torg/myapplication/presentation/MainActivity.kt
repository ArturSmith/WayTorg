package com.way_torg.myapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.way_torg.myapplication.presentation.root.DefaultRootComponent
import com.way_torg.myapplication.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as WayTorg).applicationComponent.inject(this)
        val root = rootComponentFactory.create(defaultComponentContext())
        setContent {
            RootContent(
                component = root
            )
        }
    }
}

