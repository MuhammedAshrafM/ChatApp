package com.example.chatapp.ui.new_user_info

import android.app.Activity
import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.core.BaseViewModel
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.User
import com.example.chatapp.domain.usecases.AddNewUserInfoUseCase
import com.example.chatapp.domain.usecases.UploadProfileImageUseCase
import com.example.chatapp.utils.Constants.PROFILE_IMAGE_PATH_WITH_FIRST_NAME
import com.example.chatapp.utils.PrefDataStoreUtil
import com.example.chatapp.utils.SingleLiveEvent
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewUserInfoViewModel @Inject constructor(
    private val prefUtil: PrefDataStoreUtil,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val addNewUserInfoUseCase: AddNewUserInfoUseCase
) : BaseViewModel(prefUtil) {

    private val _resultAdding = SingleLiveEvent<DataState<Unit>>()
    val resultAdding: LiveData<DataState<Unit>> = _resultAdding

    private val _downloadUri = SingleLiveEvent<DataState<Uri?>>()
    val downloadUri: LiveData<DataState<Uri?>> = _downloadUri

    private val _errorValidationResId = SingleLiveEvent<Int>()
    val errorValidationResId: LiveData<Int> = _errorValidationResId

    fun uploadProfileImage(uriImage: Uri?, uid: String){
        _downloadUri.postValue(DataState.loading())
        uploadProfileImageUseCase(uriImage, "$PROFILE_IMAGE_PATH_WITH_FIRST_NAME$uid.jpg"){
            _downloadUri.postValue(it)
        }
    }


    fun addNewUserInfo(user: User){
        val (isValid, stringResId) = verifyUserName(user.name)
        if(!isValid){
            _errorValidationResId.postValue(stringResId)
            return
        }

        _resultAdding.postValue(DataState.loading())
        addNewUserInfoUseCase(user){
            _resultAdding.postValue(it)
        }

    }

    private fun verifyUserName(name: String?): Pair<Boolean, Int?> {
        val result = if (TextUtils.isEmpty(name)) {
            false to R.string.name_user_field_mandatory
        } else
            true to null

        return result
    }


}