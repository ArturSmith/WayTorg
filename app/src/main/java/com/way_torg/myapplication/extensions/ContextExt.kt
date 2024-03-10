package com.way_torg.myapplication.extensions

import android.content.Context
import java.util.Locale


fun Context.updateLocale(locale:String) {
    val config = resources.configuration
    val newLocale = Locale(locale)
    Locale.setDefault(newLocale)
    config.setLocale(newLocale)
    createConfigurationContext(config)
    resources.updateConfiguration(config, resources.displayMetrics)
}

