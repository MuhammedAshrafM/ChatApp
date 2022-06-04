package com.example.chatapp.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import java.io.BufferedReader
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

fun dpToPx(dp: Float, context: Context): Int {
    return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}