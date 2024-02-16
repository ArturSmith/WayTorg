package com.way_torg.myapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.way_torg.myapplication.data.local.db.Dao
import com.way_torg.myapplication.data.mapper.toDto
import com.way_torg.myapplication.data.mapper.toEntity
import com.way_torg.myapplication.data.mapper.toModel
import com.way_torg.myapplication.data.network.dto.CategoryDto
import com.way_torg.myapplication.domain.entity.Category
import com.way_torg.myapplication.domain.repository.CategoryRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: Dao
) : CategoryRepository {
    override fun getAllCategoriesFromRemoteDb() = callbackFlow {
        val observer = firestore.collection(CATEGORIES).addSnapshotListener { value, error ->
            if (error != null || value?.isEmpty == true) return@addSnapshotListener

            val categories = value?.documents?.map {
                it.toObject<CategoryDto>()?.let { it.toEntity() } ?: Category.defaultInstance
            } ?: emptyList()
            trySend(categories)
        }
        awaitClose {
            observer.remove()
            this.cancel()
        }
    }

    override fun getSelectedCategoriesFromLocalDb() = dao.getCategories()
        .map {
            it.toEntity()
        }

    override suspend fun createCategory(category: Category): Result<Boolean> {
        return try {
            firestore.collection(CATEGORIES).document().set(category.toDto()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: String): Result<Boolean> {
        return try {
            firestore.collection(CATEGORIES).document(id).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun selectCategory(category: Category): Result<Boolean> {
        return try {
            dao.addCategory(category.toModel())
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unselectCategory(id: String): Result<Boolean> {
        return try {
            dao.deleteCategory(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private companion object {
        const val CATEGORIES = "categories"
    }
}