package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import com.example.weatherapp.domain.util.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllLocationSuggestions @Inject constructor(private val weatherAppRepository: WeatherAppRepository) {
    suspend operator fun invoke(queryString: String): NetworkResult<ArrayList<LocationSearchDataItem>> {
        return weatherAppRepository.fetchAllLocationList(queryString)
    }
}
