package com.example.chatapp.domain.usecases

import android.app.Activity
import com.example.chatapp.data.repositories.LoginRepositoryImpl
import com.example.chatapp.domain.base.DataState
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot

class ResendOTPCodeUseCase(private val loginRepositoryImpl: LoginRepositoryImpl) {

    fun invoke(
        activity: Activity,
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
        onLoginListener: (DataState<DocumentSnapshot?>) -> Unit,
        onCodeSentListener: (Pair<String, PhoneAuthProvider.ForceResendingToken>) -> Unit
    ) = loginRepositoryImpl.verifyPhoneNumber(
        activity,
        phoneNumber,
        token,
        { onLoginListener(it) },
        { onCodeSentListener(it) })
}