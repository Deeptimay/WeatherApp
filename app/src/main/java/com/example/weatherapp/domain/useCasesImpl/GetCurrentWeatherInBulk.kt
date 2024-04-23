package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import javax.inject.Inject

class GetCurrentWeatherInBulk @Inject constructor(private val weatherAppRepository: WeatherAppRepository) {
    suspend operator fun invoke(bulkDataRequest: BulkDataRequest): NetworkResult<FetchBulkData> {
        return weatherAppRepository.fetchCurrentWeatherDataInBulk(bulkDataRequest)
    }
}