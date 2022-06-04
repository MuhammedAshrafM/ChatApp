package com.example.chatapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.chatapp.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor
constructor(context: Context) : Interceptor {

    private val _context: Context

    init {
        _context = context
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isConnected(): Boolean {
        val connectivityManager =
            _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo?.isConnected ?: false
    }

    inner class NoConnectivityException : IOException() {
        override val message: String
            get() = _context.getString(R.string.NoInternetConnection)
    }
}
