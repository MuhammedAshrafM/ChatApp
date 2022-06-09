package com.example.chatapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.chatapp.R
import java.io.File

//fun ImageView.viewRoundedProfilePicture(context: Context, attachmentName: String?){
//    Glide.with(context)
//        .setDefaultRequestOptions(RequestOptions())
//        .load(Constants.ADMIN_URL.plus(attachmentName))
//        .transform(CircleCrop())
//        .placeholder(R.drawable.user_ex)
//        .error(R.drawable.user_ex)
//        .timeout(30000)
//        .into(this)
//}
fun ImageView.viewRoundedProfilePictureFromStorage(context: Context, uri: Uri?){
    Glide.with(context)
        .setDefaultRequestOptions(RequestOptions())
        .load(uri)
        .transform(CircleCrop())
        .placeholder(R.drawable.avatar)
        .error(R.drawable.avatar)
        .timeout(30000)
        .into(this)
}

fun ImageView.viewImageById(context: Context, resourceId: Int){
    Glide.with(context)
        .load(resourceId)
        .into(this)
}
