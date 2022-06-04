package com.example.chatapp.data.network

import com.example.chatapp.domain.base.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


fun <T> executeRemoteAPI(
    networkCall: suspend () -> DataState<T>
): Flow<DataState<T>> =
    flow {
        emit(DataState.loading())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == DataState.Status.SUCCESS) {
            emit(DataState.success(responseStatus.data))
        } else if (responseStatus.status == DataState.Status.ERROR) {
            emit(DataState.error(responseStatus.error, responseStatus.data))
        }
    }