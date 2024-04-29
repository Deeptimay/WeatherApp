package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import javax.inject.Inject

class UpdateCityListToSharedPreferences @Inject constructor(private val repository: WeatherAppRepository) {
    operator fun invoke(locationList: List<LocationBulk>) {
        repository.updateCityListToSharedPreferences(locationList)
    }
}