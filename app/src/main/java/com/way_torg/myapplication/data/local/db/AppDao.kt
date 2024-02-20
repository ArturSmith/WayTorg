package com.way_torg.myapplication.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.way_torg.myapplication.data.local.model.CategoryDbModel
import com.way_torg.myapplication.data.local.model.ProductDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(categoryDbModel: CategoryDbModel)
    @Query("DELETE FROM categories WHERE id=:id")
    suspend fun deleteCategory(id:String)
    @Query("SELECT * FROM categories")
    fun getCategories() : Flow<List<CategoryDbModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(productDbModel: ProductDbModel)
    @Query("DELETE FROM products WHERE id=:id")
    suspend fun deleteProduct(id:String)
    @Query("SELECT * FROM products")
    suspend fun getProducts():List<ProductDbModel>
    @Query("SELECT COUNT(*) FROM products")
    fun getCountOfProducts():Flow<Int>
}