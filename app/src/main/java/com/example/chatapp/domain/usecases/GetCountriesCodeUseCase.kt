package com.example.chatapp.domain.usecases

import com.example.chatapp.data.network.executeRemoteAPI
import com.example.chatapp.data.repositories.LoginRepositoryImpl

class GetCountriesCodeUseCase(private val loginRepositoryImpl: LoginRepositoryImpl) {

    suspend fun invoke() = executeRemoteAPI{
        loginRepositoryImpl.getAllCountryCodes()
    }
}