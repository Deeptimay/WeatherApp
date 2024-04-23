package com.example.weatherapp.domain.repositoryAbstraction

import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationSearchData

interface WeatherAppRepository {
    suspend fun fetchAllLocationList(queryString: String): NetworkResult<LocationSearchData>
    suspend fun fetchCurrentWeatherData(location: String): NetworkResult<CurrentWeatherData>
    suspend fun fetchCurrentWeatherDataInBulk(bulkDataRequest: BulkDataRequest): NetworkResult<FetchBulkData>
}
