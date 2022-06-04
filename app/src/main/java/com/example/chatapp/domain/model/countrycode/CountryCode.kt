package com.example.chatapp.domain.model.countrycode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryCode(
    var name: String,
    var code: String,
    var callingCode: String
) : Parcelable