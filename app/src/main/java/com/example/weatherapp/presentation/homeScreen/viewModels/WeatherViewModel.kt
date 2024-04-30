package com.example.weatherapp.presentation.homeScreen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.domain.useCasesImpl.FetchSavedCityListFromSharedPreferences
import com.example.weatherapp.domain.useCasesImpl.GetAllLocationSuggestions
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeatherInBulk
import com.example.weatherapp.domain.useCasesImpl.UpdateCityListToSharedPreferences
import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.presentation.ui.UiState
import com.example.weatherapp.presentation.util.debounce
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
    val getCurrentWeatherInBulk: GetCurrentWeatherInBulk,
    private val fetchSavedCityListFromSharedPreferences: FetchSavedCityListFromSharedPreferences,
    val updateCityListToSharedPreferences: UpdateCityListToSharedPreferences
) : ViewModel() {

    val debounceTextChange = debounce<String>(500L, viewModelScope) { fetchAllLocationSuggestions(it) }

    var searchQuery by mutableStateOf("")
        private set

    var currentWeatherList: List<LocationBulk> = listOf()

    private val _locationFlow = MutableStateFlow<ArrayList<LocationSearchDataItem>>(ArrayList<LocationSearchDataItem>())
    val locationFlow: StateFlow<ArrayList<LocationSearchDataItem>> = _locationFlow.asStateFlow()

    private val _currentWeatherFlowInBulk = MutableStateFlow<UiState>(UiState.Loading)
    val currentWeatherFlowInBulk: StateFlow<UiState> = _currentWeatherFlowInBulk.asStateFlow()

    init {
        fetchAllLocationWeatherInBulk()
    }

    fun fetchAllLocationWeatherInBulk() {
        currentWeatherList = fetchSavedCityListFromSharedPreferences()
        if (currentWeatherList.isEmpty()) {
            _currentWeatherFlowInBulk.update { UiState.Success("") }
        } else {
            val bulkDataRequest = BulkDataRequest(currentWeatherList)
            fetchCurrentWeatherByCityInBulk(bulkDataRequest)
        }
    }

    fun fetchAllLocationSuggestions(query: String) {
        viewModelScope.launch {
            val response = getAllLocationSuggestions(query)
            _locationFlow.update {
                when (response) {
                    is NetworkResult.ApiError -> ArrayList<LocationSearchDataItem>()
                    is NetworkResult.ApiSuccess -> response.data
                }
            }
        }
    }

    fun fetchCurrentWeatherByCityInBulk(bulkDataRequest: BulkDataRequest) {
        viewModelScope.launch {
            val response = getCurrentWeatherInBulk(bulkDataRequest)
            onSearchQueryChange("")
            _currentWeatherFlowInBulk.update {
                when (response) {
                    is NetworkResult.ApiError -> UiState.Error(response.errorData)
                    is NetworkResult.ApiSuccess -> UiState.Success(response.data)
                }
            }
        }
    }

    fun fetchCurrentWeatherOnCitySelectionController(location: String) {
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

        currentWeatherList = placePreferenceDataMutable
        updateCityListToSharedPreferences(placePreferenceDataMutable)
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        if (newQuery.isNotEmpty())
            debounceTextChange(searchQuery)
        else
            _locationFlow.value = ArrayList<LocationSearchDataItem>()
    }
}
