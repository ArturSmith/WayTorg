package com.way_torg.myapplication.di

import com.way_torg.myapplication.presentation.MainActivity
import dagger.Component
@ApplicationScope
@Component
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
}