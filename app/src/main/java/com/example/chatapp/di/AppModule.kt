package com.example.chatapp.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.chatapp.MainActivity
import com.example.chatapp.core.BaseApplication
import com.example.chatapp.utils.PrefDataStoreUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePrefDataStoreUtil(@ApplicationContext context: Context): PrefDataStoreUtil =
        PrefDataStoreUtil(context)



}