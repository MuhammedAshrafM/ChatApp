package com.example.chatapp.data.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

//    @Headers("Authorization: key=${BuildConfig.SERVER_KEY}", "Content-Type:$CONTENT_TYPE")
//    @POST("fcm/send")
//    suspend fun sendNotification(
//        @Body notification: PushNotification
//    ): Response<ResponseBody>

//    @Multipart
//    @POST("changePic")
//    fun uploadUserProfile(
//        @Part("id") id: RequestBody?,
//        @Part image: MultipartBody.Part?
//    ): Call<Result>

}