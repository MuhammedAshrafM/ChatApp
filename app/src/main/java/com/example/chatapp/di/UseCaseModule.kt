package com.example.chatapp.di

import com.example.chatapp.data.repositories.LoginRepositoryImpl
import com.example.chatapp.domain.usecases.GetCountriesCodeUseCase
import com.example.chatapp.domain.usecases.LoginUseCase
import com.example.chatapp.domain.usecases.ResendOTPCodeUseCase
import com.example.chatapp.domain.usecases.VerifyOTPCodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetCountriesCodeUseCase(loginRepository: LoginRepositoryImpl) =
        GetCountriesCodeUseCase(loginRepository)

    @Singleton
    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepositoryImpl) =
        LoginUseCase(loginRepository)

    @Singleton
    @Provides
    fun provideVerifyOTPCodeUseCase(loginRepository: LoginRepositoryImpl) =
        VerifyOTPCodeUseCase(loginRepository)

    @Singleton
    @Provides
    fun provideResendOTPCodeUseCase(loginRepository: LoginRepositoryImpl) =
        ResendOTPCodeUseCase(loginRepository)
}