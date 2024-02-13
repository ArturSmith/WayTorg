package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.Filter
import kotlinx.coroutines.flow.Flow

interface FilterRepository {

    fun getAllFilters() : Flow<List<Filter>>

}