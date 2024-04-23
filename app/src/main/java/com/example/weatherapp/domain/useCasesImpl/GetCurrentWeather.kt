package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import javax.inject.Inject

class GetCurrentWeather @Inject constructor(private val weatherAppRepository: WeatherAppRepository) {
    suspend operator fun invoke(location: String): NetworkResult<CurrentWeatherData> {
        return weatherAppRepository.fetchCurrentWeatherData(location)
    }
}