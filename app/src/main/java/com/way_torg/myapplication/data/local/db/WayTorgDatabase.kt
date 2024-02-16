package com.way_torg.myapplication.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.way_torg.myapplication.data.local.model.CategoryDbModel
import com.way_torg.myapplication.data.local.model.ProductDbModel

@Database(entities = [ProductDbModel::class, CategoryDbModel::class], version = 1, exportSchema = false)
abstract class WayTorgDatabase():RoomDatabase() {
    abstract fun dao() : Dao

    companion object {
        private const val NAME = "wayTorgDatabase"
        private var INSTANCE : WayTorgDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): WayTorgDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK){
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = WayTorgDatabase::class.java,
                    name = NAME
                ).build()

                INSTANCE = database
                return database
            }
        }
    }
}