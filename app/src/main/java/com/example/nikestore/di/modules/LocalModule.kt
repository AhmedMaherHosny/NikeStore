package com.example.nikestore.di.modules

import android.content.Context
import com.example.domain.repository.local.DatastoreRepository
import com.example.local.repository.DatastoreRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext context: Context,
        gson: Gson
    ): DatastoreRepository {
        return DatastoreRepositoryImpl(context, gson)
    }
}