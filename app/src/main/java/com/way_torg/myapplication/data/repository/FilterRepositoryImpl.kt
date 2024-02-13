package com.way_torg.myapplication.data.repository

import com.way_torg.myapplication.domain.entity.Filter
import com.way_torg.myapplication.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(): FilterRepository {
    override fun getAllFilters(): Flow<List<Filter>> {
        return flow {  }
    }
}