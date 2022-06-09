package com.example.chatapp.utils

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import kotlin.math.roundToInt

fun readFile(context: Context, assetFileName: String): String {
    var reader: BufferedReader? = null
    var jsonString = ""
    try {
        reader = BufferedReader(
            InputStreamReader(
                context.assets.open(assetFileName),
                "UTF-8"
            )
        )
        var line = reader.readLine()
        while (line != null) {
            jsonString += line
            line = reader.readLine()
        }

    } catch (e: Exception) {
        Log.d("Tag",e.localizedMessage ?: "Unknown")
    } finally {
        reader?.let {
            try {
                reader.close()
            } catch (e: Exception) {
                Log.d("Tag",e.localizedMessage ?: "Unknown")
            }
        }
    }

    return jsonString
}

fun getBytesFromUriImage(context: Context, uri: Uri?): ByteArray?{
    uri?.let {
        return try {
            val realPath = RealPathUtil.getRealPath(context, uri)
            val file = Uri.fromFile(File(realPath))

            val bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, file)
            val byteStreamArray = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteStreamArray)

            byteStreamArray.toByteArray()
        }catch (e: Exception){
            null
        }
    }

    return null
}

fun dpToPx(dp: Float, context: Context): Int {
    return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}