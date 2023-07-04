package com.example.nikestore.di.modules

import com.example.domain.repository.remote.FirebaseRepository
import com.example.remote.repository.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideFirebaseRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFireStore: FirebaseFirestore
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseAuth, firebaseFireStore)
    }

}