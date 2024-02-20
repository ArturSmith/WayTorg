package com.way_torg.myapplication.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.way_torg.myapplication.data.local.db.AppDao
import com.way_torg.myapplication.data.local.db.AppDatabase
import com.way_torg.myapplication.data.repository.CategoryRepositoryImpl
import com.way_torg.myapplication.data.repository.ProductRepositoryImpl
import com.way_torg.myapplication.domain.repository.CategoryRepository
import com.way_torg.myapplication.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @[ApplicationScope Binds]
    fun bindFilterRepository(repository: CategoryRepositoryImpl): CategoryRepository


    companion object {
        @[ApplicationScope Provides]
        fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

        @[ApplicationScope Provides]
        fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

        @[ApplicationScope Provides]
        fun provideAppDatabase(context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideAppDao(appDatabase: AppDatabase): AppDao {
            return appDatabase.appDao()
        }
    }

}