package com.way_torg.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow


interface LocaleRepository {
    suspend fun saveLocale()
     fun getLocale(): Flow<String>
}