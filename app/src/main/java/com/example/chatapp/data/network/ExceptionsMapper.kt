package com.example.chatapp.data.network

import android.content.Context
import com.example.chatapp.R
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ExceptionsMapper constructor(private val context: Context) {

    fun mapFromException(exception: Throwable?) =
        if (exception != null)
            when (exception) {
                is NetworkConnectionInterceptor.NoConnectivityException ->
                    context.getString(R.string.failedConnection)
                is TimeoutException, is UnknownHostException, is SocketTimeoutException ->
                    context.getString(R.string.timeoutExceeded)
                is FirebaseAuthInvalidCredentialsException ->
                    context.getString(R.string.invalid_auth_request)
                is FirebaseTooManyRequestsException ->
                    context.getString(R.string.too_many_requests)
                else -> context.getString(R.string.error_occurred)
            }
        else
            context.getString(R.string.error_occurred)


//    companion object{
//        fun resolveError(e: Exception): State.ErrorState {
//            var error = e
//
//            when (e) {
//                is SocketTimeoutException -> {
//                    error = NetworkErrorException(errorMessage = "connection error!")
//                }
//                is ConnectException -> {
//                    error = NetworkErrorException(errorMessage = "no internet access!")
//                }
//                is UnknownHostException -> {
//                    error = NetworkErrorException(errorMessage = "no internet access!")
//                }
//            }
//
//            if(e is HttpException){
//                when(e.code()){
//                    502 -> {
//                        error = NetworkErrorException(e.code(),  "internal error!")
//                    }
//                    401 -> {
//                        throw AuthenticationException("authentication error!")
//                    }
//                    400 -> {
//                        error = NetworkErrorException.parseException(e)
//                    }
//                }
//            }
//
//
//            return State.ErrorState(error)
//        }
//    }
}





















