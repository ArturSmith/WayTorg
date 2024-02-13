package com.way_torg.myapplication.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.way_torg.myapplication.data.repository.FilterRepositoryImpl
import com.way_torg.myapplication.data.repository.ProductRepositoryImpl
import com.way_torg.myapplication.domain.repository.FilterRepository
import com.way_torg.myapplication.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @[ApplicationScope Binds]
    fun bindFilterRepository(repository: FilterRepositoryImpl): FilterRepository


    companion object {
        @[ApplicationScope Provides]
        fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

        @[ApplicationScope Provides]
        fun provideFirebaseStorage():FirebaseStorage = FirebaseStorage.getInstance()
    }

}