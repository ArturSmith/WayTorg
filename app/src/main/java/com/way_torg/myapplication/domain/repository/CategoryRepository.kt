package com.way_torg.myapplication.domain.repository

import com.way_torg.myapplication.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesFromRemoteDb(): Flow<List<Category>>
    fun getSelectedCategoriesFromLocalDb(): Flow<List<Category>>

    suspend fun createCategory(category: Category): Result<Boolean>
    suspend fun deleteCategory(id: String): Result<Boolean>

    suspend fun selectCategory(category: Category) : Result<Boolean>
    suspend fun unselectCategory(id: String): Result<Boolean>

}