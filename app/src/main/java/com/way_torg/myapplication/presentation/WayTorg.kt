package com.way_torg.myapplication.presentation

import android.app.Application
import com.way_torg.myapplication.di.ApplicationComponent
import com.way_torg.myapplication.di.DaggerApplicationComponent

class WayTorg:Application() {
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}