package com.way_torg.myapplication.data.repository

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.way_torg.myapplication.domain.repository.LocaleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LocaleRepositoryImpl @Inject constructor(
    sharedPreferences: SharedPreferences,
    private val sharedEditor: Editor
) : LocaleRepository {

    private val _currentLocale =
        MutableStateFlow(sharedPreferences.getString(LOCALE, null) ?: DEFAULT_LOCALE)
    val currentLocale = _currentLocale.asStateFlow()
    override suspend fun saveLocale() {
        val currentLocale = _currentLocale.value
        val newLocale = if (currentLocale == LOCALE_RU) LOCALE_EN else LOCALE_RU
        with(sharedEditor) {
            putString(LOCALE, newLocale)
            commit()
        }
        _currentLocale.emit(newLocale)
    }

    override fun getLocale() = currentLocale

    private companion object {
        const val LOCALE = "locale"
        const val DEFAULT_LOCALE = "RU"
        const val LOCALE_RU = "ru"
        const val LOCALE_EN = "en"
    }
}