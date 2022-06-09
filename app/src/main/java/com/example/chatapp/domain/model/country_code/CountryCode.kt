package com.example.chatapp.domain.model.country_code

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryCode(
    var name: String,
    var code: String,
    var callingCode: String
) : Parcelable