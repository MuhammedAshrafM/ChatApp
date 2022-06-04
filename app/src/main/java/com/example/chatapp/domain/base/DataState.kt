package com.example.chatapp.domain.base

data class DataState<out T>(
    val status: Status = Status.SUCCESS,
    val data: T? = null,
    val error: HttpError? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): DataState<T> {
            return DataState(Status.SUCCESS, data, null)
        }

        fun <T> error(error: HttpError?, data: T? = null): DataState<T> {
            return DataState(Status.ERROR, data, error)
        }

        fun <T> loading(data: T? = null): DataState<T> {
            return DataState(Status.LOADING, data, null)
        }
    }

    class HttpError(
        val serverMessage: String?,
        val throwableMessage: String?,
        val errorCode: Int = 0
    )
}