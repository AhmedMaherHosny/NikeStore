package com.example.nikestore.di.modules

import android.content.Context
import androidx.room.Room
import com.example.core.Constants.DATABASE_NAME
import com.example.domain.repository.local.DatastoreRepository
import com.example.domain.repository.local.RoomDbRepository
import com.example.local.db.AppDataBase
import com.example.local.db.dao.AddressDao
import com.example.local.repository.DatastoreRepositoryImpl
import com.example.local.repository.RoomDbRepositoryImpl
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
    ): DatastoreRepository =
        DatastoreRepositoryImpl(context, gson)


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDataBase =
        Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            DATABASE_NAME
        ).build()


    @Singleton
    @Provides
    fun provideAddressDao(appDataBase: AppDataBase): AddressDao = appDataBase.addressDao()

    @Singleton
    @Provides
    fun provideRoomDbRepository(addressDao: AddressDao): RoomDbRepository =
        RoomDbRepositoryImpl(addressDao)

}