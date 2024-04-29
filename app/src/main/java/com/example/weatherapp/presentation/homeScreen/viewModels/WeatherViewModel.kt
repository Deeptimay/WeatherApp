package com.example.weatherapp.presentation.homeScreen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.Location
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchData
import com.example.weatherapp.domain.useCasesImpl.FetchSavedCityListFromSharedPreferences
import com.example.weatherapp.domain.useCasesImpl.GetAllLocationSuggestions
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeather
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeatherInBulk
import com.example.weatherapp.domain.useCasesImpl.UpdateCityListToSharedPreferences
import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.presentation.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getAllLocationSuggestions: GetAllLocationSuggestions,
    private val getCurrentWeather: GetCurrentWeather,
    private val getCurrentWeatherInBulk: GetCurrentWeatherInBulk,
    private val fetchSavedCityListFromSharedPreferences: FetchSavedCityListFromSharedPreferences,
    private val updateCityListToSharedPreferences: UpdateCityListToSharedPreferences
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var currentWeatherList: List<LocationBulk> = listOf()

    private val _locationFlow = MutableStateFlow<LocationSearchData>(LocationSearchData())
    val locationFlow: StateFlow<LocationSearchData> = _locationFlow.asStateFlow()

    private val _selectedLocationFlow = MutableStateFlow<Location>(Location())
    val selectedLocationFlow: StateFlow<Location> = _selectedLocationFlow.asStateFlow()

    private val _currentWeatherFlow = MutableStateFlow<UiState>(UiState.Loading)
    val currentWeatherFlow: StateFlow<UiState> = _currentWeatherFlow.asStateFlow()

    private val _currentWeatherFlowInBulk = MutableStateFlow<UiState>(UiState.Loading)
    val currentWeatherFlowInBulk: StateFlow<UiState> = _currentWeatherFlowInBulk.asStateFlow()

    private val _showProgressBar = MutableStateFlow<Boolean>(false)
    val showProgressBar: StateFlow<Boolean> = _showProgressBar.asStateFlow()

    init {
        currentWeatherList = fetchSavedCityListFromSharedPreferences()
        val bulkDataRequest = BulkDataRequest(currentWeatherList)
        fetchCurrentWeatherByCityInBulk(bulkDataRequest)
    }

    private fun fetchAllLocationSuggestions(query: String) {
        _selectedLocationFlow.value.name = query
        viewModelScope.launch {
            val response = getAllLocationSuggestions(query)
            _locationFlow.update {
                when (response) {
                    is NetworkResult.ApiError -> LocationSearchData()
                    is NetworkResult.ApiSuccess -> response.data
                }
            }
        }
    }

    fun fetchCurrentWeatherByCity(location: String) {
        viewModelScope.launch {
            val response = getCurrentWeather(location)
            _currentWeatherFlow.update {
                when (response) {
                    is NetworkResult.ApiError -> UiState.Error(response.errorData)
                    is NetworkResult.ApiSuccess -> UiState.Success(response.data)
                }
            }
        }
    }

    private fun fetchCurrentWeatherByCityInBulk(bulkDataRequest: BulkDataRequest) {
        viewModelScope.launch {
            val response = getCurrentWeatherInBulk(bulkDataRequest)
            _currentWeatherFlowInBulk.update {
                when (response) {
                    is NetworkResult.ApiError -> UiState.Error(response.errorData)
                    is NetworkResult.ApiSuccess -> UiState.Success(response.data)
                }
            }
        }
    }

    fun fetchCurrentWeatherByCityController(location: String) {
        onSearchQueryChange("")
        val locationList = listOf(LocationBulk(location, System.currentTimeMillis().toString()))
        val placePreferenceData = fetchSavedCityListFromSharedPreferences()

        currentWeatherList = locationList.plus(placePreferenceData)
        val bulkDataRequest = BulkDataRequest(currentWeatherList)

        updateCityListToSharedPreferences(currentWeatherList)
        fetchCurrentWeatherByCityInBulk(bulkDataRequest)
    }

    fun removeSwipedWeatherByCityController(bulk: Bulk) {
        val placePreferenceData = fetchSavedCityListFromSharedPreferences()
        val placePreferenceDataMutable = placePreferenceData.toMutableList()
        placePreferenceDataMutable.removeIf { it.custom_id == bulk.query.custom_id }
        updateCityListToSharedPreferences(placePreferenceDataMutable)
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        if (newQuery.isNotEmpty())
            fetchAllLocationSuggestions(searchQuery)
        else
            _locationFlow.value = LocationSearchData()
    }
}
