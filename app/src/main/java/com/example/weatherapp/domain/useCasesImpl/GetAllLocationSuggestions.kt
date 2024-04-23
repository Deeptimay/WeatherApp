package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.data.models.LocationSearchData
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllLocationSuggestions @Inject constructor(private val weatherAppRepository: WeatherAppRepository) {
    suspend operator fun invoke(queryString: String): NetworkResult<LocationSearchData> {
        return weatherAppRepository.fetchAllLocationList(queryString)
    }
}
