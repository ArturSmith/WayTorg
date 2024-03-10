package com.way_torg.myapplication.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.way_torg.myapplication.domain.use_case.GetLocaleUseCase
import com.way_torg.myapplication.extensions.updateLocale
import com.way_torg.myapplication.presentation.root.DefaultRootComponent
import com.way_torg.myapplication.presentation.root.RootContent
import com.way_torg.myapplication.presentation.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as WayTorg).applicationComponent.inject(this)
        val root = rootComponentFactory.create(defaultComponentContext())

        changeLocale(this)
        setContent {
            MyApplicationTheme {
                RootContent(
                    component = root
                )
            }
        }
    }

    private fun changeLocale(context: Context) {
        lifecycleScope.launch {
            viewModel.getCurrentLocale.collect {
                context.updateLocale(it)
            }
        }
    }
}


