package com.example.chatapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid: String? = "",
    var name: String? = "",
    var imageUrl: String? = "",
    var bio: String? = "",
    var groups: List<String>? = emptyList(),
    var phoneNumber: String? = "",
    var notificationToken: String? = "",
    var blockList: List<String> = emptyList()
) : Parcelable
