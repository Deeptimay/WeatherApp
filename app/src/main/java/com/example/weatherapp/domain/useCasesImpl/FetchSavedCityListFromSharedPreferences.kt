package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import javax.inject.Inject

class FetchSavedCityListFromSharedPreferences @Inject constructor(private val weatherAppRepository: WeatherAppRepository) {
    operator fun invoke(): List<LocationBulk> {
        return weatherAppRepository.fetchSavedCityListFromSharedPreferences()
    }
}