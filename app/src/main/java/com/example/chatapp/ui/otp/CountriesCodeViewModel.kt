package com.example.chatapp.ui.otp

import androidx.lifecycle.*
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.countrycode.CountryCode
import com.example.chatapp.domain.usecases.GetCountriesCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesCodeViewModel @Inject constructor(
    private val getCountriesCodeUseCase: GetCountriesCodeUseCase,
    state: SavedStateHandle
): ViewModel() {

    private val _countriesCodesSF: MutableStateFlow<DataState<List<CountryCode>>> =
        MutableStateFlow(DataState())
    val countriesCodesSF = _countriesCodesSF.asStateFlow()

    private val _searchQuery: MutableLiveData<String> =
        state.getLiveData("SEARCH_QUERY", "")
    val searchQuery: LiveData<String> = _searchQuery

    private val _countriesCodesFilteredSF = MutableLiveData<List<CountryCode>?>()
    val countriesCodesFilteredSF: LiveData<List<CountryCode>?>  = _countriesCodesFilteredSF

    init {
        getCountriesCodes()
    }

    private fun getCountriesCodes() {
        viewModelScope.launch {
//            _countriesCodesSF.emit(DataState.loading())
            getCountriesCodeUseCase.invoke()
                .buffer()
                .onEach { _countriesCodesSF.emit(it) }
                .launchIn(viewModelScope)
        }
    }

    fun searchCountry(queryText: String) {
        _countriesCodesSF.value.let { countries ->
            val countryNameOrCallingCode =
                if (queryText.startsWith('+')) queryText.removeRange(0..0)
                else queryText
            val countriesFiltered =
                countries.data?.filter {
                    it.name.lowercase().contains(countryNameOrCallingCode.trim().lowercase()) ||
                            it.callingCode.contains(countryNameOrCallingCode.trim())
                }
            _countriesCodesFilteredSF.postValue(countriesFiltered)
        }
    }

    fun setSearchQueryValue(country: String) {
        _searchQuery.postValue(country)
    }
}