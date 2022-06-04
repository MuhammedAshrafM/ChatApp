package com.example.chatapp.domain.usecases

import android.app.Activity
import com.example.chatapp.data.repositories.LoginRepositoryImpl
import com.example.chatapp.domain.base.DataState
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot

class VerifyOTPCodeUseCase(private val loginRepositoryImpl: LoginRepositoryImpl) {

    fun invoke(
        activity: Activity,
        otpCode: String,
        verificationId: String?,
        onVerifyListener: (DataState<DocumentSnapshot?>) -> Unit
    ) = loginRepositoryImpl.verifyOTPCode(activity, otpCode, verificationId){
        onVerifyListener(it)
    }
}