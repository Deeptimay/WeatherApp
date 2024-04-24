package com.example.weatherapp.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.domain.useCasesImpl.GetAllLocationSuggestions
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeather
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeatherInBulk
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
class ReposViewModel @Inject constructor(
    private val getAllLocationSuggestions: GetAllLocationSuggestions,
    private val getCurrentWeather: GetCurrentWeather,
    private val getCurrentWeatherInBulk: GetCurrentWeatherInBulk
) : ViewModel() {

    private val _locationFlow = MutableStateFlow<UiState>(UiState.Loading)
    val locationFlow: StateFlow<UiState> = _locationFlow.asStateFlow()

    private val _currentWeatherFlow = MutableStateFlow<UiState>(UiState.Loading)
    val currentWeatherFlow: StateFlow<UiState> = _currentWeatherFlow.asStateFlow()

    private val _currentWeatherFlowInBulk = MutableStateFlow<UiState>(UiState.Loading)
    val currentWeatherFlowInBulk: StateFlow<UiState> = _currentWeatherFlowInBulk.asStateFlow()

    fun fetchAllLocationSuggestions(query: String) {
        viewModelScope.launch {
            val response = getAllLocationSuggestions(query)
            _locationFlow.update {
                when (response) {
                    is NetworkResult.ApiError -> UiState.Error(response.errorData)
                    is NetworkResult.ApiSuccess -> UiState.Success(response.data)
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

    fun fetchCurrentWeatherByCityInBulk(bulkDataRequest: BulkDataRequest) {
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
}
