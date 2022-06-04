package com.example.chatapp.core

import androidx.lifecycle.ViewModel
import com.example.chatapp.utils.PrefDataStoreUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel constructor(prefUtil: PrefDataStoreUtil) : ViewModel() {

    private val _prefUtilSF: MutableStateFlow<PrefDataStoreUtil> = MutableStateFlow(prefUtil)

    val prefUtilSF = _prefUtilSF.asStateFlow()

}