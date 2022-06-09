package com.example.chatapp.ui.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.R
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.usecases.LoginUseCase
import com.example.chatapp.domain.usecases.ResendOTPCodeUseCase
import com.example.chatapp.domain.usecases.VerifyOTPCodeUseCase
import com.example.chatapp.utils.SingleLiveEvent
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val verifyOTPCodeUseCase: VerifyOTPCodeUseCase,
    private val resendOTPCodeUseCase: ResendOTPCodeUseCase
) : ViewModel() {

    private val _countryCode = MutableLiveData<String>()
    val countryCode: LiveData<String> = _countryCode

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    private val _errorValidationResId = SingleLiveEvent<Int>()
    val errorValidationResId: LiveData<Int> = _errorValidationResId

    private val _documentSnapshot = SingleLiveEvent<DataState<DocumentSnapshot?>>()
    val documentSnapshot: LiveData<DataState<DocumentSnapshot?>> = _documentSnapshot

    private val _verificationCodeInfo = SingleLiveEvent<Pair<String, PhoneAuthProvider.ForceResendingToken?>>()
    val verificationCodeInfo: LiveData<Pair<String, PhoneAuthProvider.ForceResendingToken?>> = _verificationCodeInfo


    fun login(activity: Activity, phoneNumber: String){
        _phoneNumber.value = phoneNumber
        val (isValid, stringResId) = verifyPhoneNumber(_countryCode.value, phoneNumber)
        if(!isValid){
            _errorValidationResId.postValue(stringResId)
            return
        }

        _documentSnapshot.postValue(DataState.loading())

        loginUseCase(activity, _countryCode.value + phoneNumber, {
            _documentSnapshot.postValue(it)
        }){
            _verificationCodeInfo.postValue(it)
        }

    }

    private fun verifyPhoneNumber(code: String?, phoneNumber: String): Pair<Boolean, Int?> {
        val result = if (TextUtils.isEmpty(code)) {
            false to R.string.code_field_mandatory
        } else if (TextUtils.isEmpty(phoneNumber)) {
            false to R.string.phone_no_field_mandatory
        } else if (!phoneNumber.matches(Regex(Patterns.PHONE.pattern()))) {
            false to R.string.phone_no_invalid
        } else
            true to null

        return result
    }

    fun verifyOTPCode(activity: Activity, otpCode: String) {
        _documentSnapshot.postValue(DataState.loading())
        val verificationId = _verificationCodeInfo.value?.first
        verifyOTPCodeUseCase(activity, otpCode, verificationId){
            _documentSnapshot.postValue(it)
        }
    }

    fun resendOTP(activity: Activity) {
        _documentSnapshot.postValue(DataState.loading())
        resendOTPCodeUseCase(activity, _countryCode.value + _phoneNumber.value, _verificationCodeInfo.value?.second, {
            _documentSnapshot.postValue(it)
        }){
            _verificationCodeInfo.postValue(it)
        }
    }

    fun setCountryCode(countryCode: String) {
        _countryCode.value = countryCode
    }


}