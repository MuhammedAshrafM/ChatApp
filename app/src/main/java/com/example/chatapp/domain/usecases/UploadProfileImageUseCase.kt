package com.example.chatapp.domain.usecases

import android.app.Activity
import android.net.Uri
import com.example.chatapp.data.repositories.AddNewUserInfoRepositoryImpl
import com.example.chatapp.data.repositories.LoginRepositoryImpl
import com.example.chatapp.domain.base.DataState
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot

class UploadProfileImageUseCase(private val addNewUserInfoRepositoryImpl: AddNewUserInfoRepositoryImpl) {

    operator fun invoke(
        uriImage: Uri?,
        pathStorage: String,
        onUploadListener: (DataState<Uri?>) -> Unit
    ) = addNewUserInfoRepositoryImpl.uploadProfileImage(
        uri = uriImage,
        path = pathStorage,
        onUploadListener = { onUploadListener(it) })
}