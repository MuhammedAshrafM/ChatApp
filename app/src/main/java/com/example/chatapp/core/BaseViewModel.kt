package com.example.chatapp.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.utils.PrefDataStoreUtil
import kotlinx.coroutines.launch

open class BaseViewModel constructor(private val prefUtil: PrefDataStoreUtil) : ViewModel() {

    protected val _isFirstTimeSF: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFirstTimeSF: LiveData<Boolean> = _isFirstTimeSF

    protected val _isLoggedSF: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isLoggedSF: LiveData<Boolean> = _isLoggedSF


    fun disableFirstTime(){
        viewModelScope.launch {
            val firstTime = prefUtil.enableFirstTime(false)
            _isFirstTimeSF.postValue(firstTime)
        }
    }

    fun setUserAsLogged(){
        viewModelScope.launch {
            val isLogged = prefUtil.setUserAsLogged(true)
            _isLoggedSF.postValue(isLogged)
        }
    }

    fun saveUID(uid: String) {
        viewModelScope.launch {
            prefUtil.saveUID(uid)
        }
    }
}