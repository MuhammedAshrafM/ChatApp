package com.example.chatapp.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.core.BaseViewModel
import com.example.chatapp.domain.model.SliderItems
import com.example.chatapp.utils.PrefDataStoreUtil
import com.meem.domain.base.ResponseApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val prefUtil: PrefDataStoreUtil): BaseViewModel(prefUtil) {

    private val _sliderItemsSF: MutableStateFlow<SliderItems> =
        MutableStateFlow(SliderItems())
    val sliderItemsSF = _sliderItemsSF.asStateFlow()

}