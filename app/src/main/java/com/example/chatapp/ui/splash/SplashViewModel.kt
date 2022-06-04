package com.example.chatapp.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.core.BaseViewModel
import com.example.chatapp.domain.model.SliderItems
import com.example.chatapp.utils.PrefDataStoreUtil
import com.meem.domain.base.ResponseApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(prefUtil: PrefDataStoreUtil) : BaseViewModel(prefUtil)  {

    private val _isFirstTimeSF: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isFirstTimeSF = _isFirstTimeSF.asStateFlow()

    private val _isLoggedSF: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedSF = _isLoggedSF.asStateFlow()

    init {
        viewModelScope.launch() {
            try {
                val asyncFirstTime = async { prefUtil.isAppFirstTime() }
                val asyncLogged = async { prefUtil.isUserLogged() }

                asyncFirstTime.await().buffer().onEach { _isFirstTimeSF.emit(it) }.launchIn(viewModelScope)
                asyncLogged.await().buffer().onEach { _isLoggedSF.emit(it) }.launchIn(viewModelScope)
            } catch (e: Exception) {
                Log.d("TAG", "setAnimation : $e")
            }
        }
    }

}