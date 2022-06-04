package com.example.chatapp.di

import android.content.Context
import com.example.chatapp.ui.onboarding.SliderAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {


    @Provides
    @Singleton
    fun provideSliderAdapter(@ApplicationContext context : Context) = SliderAdapter(context)

}