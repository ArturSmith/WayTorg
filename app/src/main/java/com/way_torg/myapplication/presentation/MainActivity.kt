package com.way_torg.myapplication.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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


