package com.example.chatapp.di

import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.chatapp.data.network.ExceptionsMapper
import com.example.chatapp.data.repositories.AddNewUserInfoRepositoryImpl
import com.example.chatapp.data.repositories.LoginRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(
        exceptionsMapper: ExceptionsMapper,
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ) =
        LoginRepositoryImpl(exceptionsMapper, context, firebaseAuth, firebaseFirestore)

    @Singleton
    @Provides
    fun provideAddNewUserInfoRepositoryImpl(
        exceptionsMapper: ExceptionsMapper,
        @ApplicationContext context: Context,
        firesStorage: FirebaseStorage,
        fireStore: FirebaseFirestore
    ) =
        AddNewUserInfoRepositoryImpl(exceptionsMapper, context, firesStorage, fireStore)
}