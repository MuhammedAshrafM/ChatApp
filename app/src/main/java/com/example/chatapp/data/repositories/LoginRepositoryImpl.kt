package com.example.chatapp.data.repositories

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.chatapp.data.network.ExceptionsMapper
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.countrycode.Countries
import com.example.chatapp.domain.model.countrycode.CountryCode
import com.example.chatapp.utils.Constants.USERS_ID
import com.example.chatapp.utils.readFile
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val exceptionsMapper: ExceptionsMapper,
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) {

    suspend fun getAllCountryCodes(): DataState<List<CountryCode>> =
        withContext(Dispatchers.IO) {
            val gson = Gson()
            try {
                val allCountries =
                    gson.fromJson(readFile(context, "CountriesCode.json"), Countries::class.java)
                DataState.success(allCountries.countries.sortedBy { it.name })

            } catch (e: Exception) {
                DataState.error(
                    DataState.HttpError(
                        null,
                        exceptionsMapper.mapFromException(e)
                    ), emptyList()
                )
            }
        }

    fun verifyPhoneNumber(
        activity: Activity,
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken? = null,
        onLoginListener: (DataState<DocumentSnapshot?>) -> Unit,
        onCodeSentListener: (Pair<String, PhoneAuthProvider.ForceResendingToken>) -> Unit
    ) {
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                signInWithPhoneAuthCredential(activity, credential) {
                    onLoginListener(it)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

//                val msg = e.localizedMessage?.let {
//                    if (it.contains("A network error")) "Check Internet connection!"
//                    else "Number format is incorrect please enter a correct number"
//                } ?: "Code not sent"

                Log.d("TAG", "onVerificationFailed: $e")
                onLoginListener(
                    DataState(
                        DataState.Status.ERROR,
                        null,
                        DataState.HttpError(null, exceptionsMapper.mapFromException(e))
                    )
                )
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:$verificationId")

                onCodeSentListener(verificationId to token)
            }
        }


        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)

        token?.let {
            options.setForceResendingToken(it)
        }

        PhoneAuthProvider.verifyPhoneNumber(options.build())
    }


    private fun signInWithPhoneAuthCredential(
        activity: Activity,
        credential: PhoneAuthCredential,
        onVerificationListener: (DataState<DocumentSnapshot?>) -> Unit
    ) =
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user

                    user?.let {
                        checkIfUserIsExist(it.uid) {
                            onVerificationListener(it)
                        }
                    }
                } else {

                    Log.d("TAG", "onVerificationFailed:// ${task.exception}")
                    onVerificationListener(
                        DataState(
                            DataState.Status.ERROR,
                            null,
                            DataState.HttpError(
                                null,
                                exceptionsMapper.mapFromException(task.exception)
                            )
                        )
                    )
                }
            }
            .addOnFailureListener {

                Log.d("TAG", "onVerificationFailed:** $it")
                onVerificationListener(
                    DataState(
                        DataState.Status.ERROR,
                        null,
                        DataState.HttpError(null, exceptionsMapper.mapFromException(it))
                    )
                )
            }


    private fun checkIfUserIsExist(
        uid: String,
        onUserExistListener: (DataState<DocumentSnapshot?>) -> Unit
    ) =
        fireStore.collection(USERS_ID).document(uid).get()
            .addOnSuccessListener {
                onUserExistListener(DataState(data = it))
            }
            .addOnFailureListener {

                Log.d("TAG", "onVerificationFailed:-- $it")
                onUserExistListener(
                    DataState(
                        DataState.Status.ERROR,
                        null,
                        DataState.HttpError(null, exceptionsMapper.mapFromException(it))
                    )
                )
            }

    fun verifyOTPCode(
        activity: Activity,
        otpCode: String,
        verificationId: String?,
        onVerifyListener: (DataState<DocumentSnapshot?>) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otpCode)

        signInWithPhoneAuthCredential(activity, credential) {
            onVerifyListener(it)
        }
    }
}