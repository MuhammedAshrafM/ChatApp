package com.example.chatapp.domain.usecases

import android.app.Activity
import android.net.Uri
import com.example.chatapp.data.repositories.AddNewUserInfoRepositoryImpl
import com.example.chatapp.data.repositories.LoginRepositoryImpl
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.User
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot

class AddNewUserInfoUseCase(private val addNewUserInfoRepositoryImpl: AddNewUserInfoRepositoryImpl) {

    operator fun invoke(
        user: User,
        onAddedListener: (DataState<Unit>) -> Unit
    ) = addNewUserInfoRepositoryImpl.addNewUserInfo(
        user = user,
        onAddedListener = { onAddedListener(it) })
}