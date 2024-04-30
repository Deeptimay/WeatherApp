package com.example.weatherapp.domain.repositoryAbstraction

import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.domain.util.NetworkResult

interface WeatherAppRepository {
    suspend fun fetchAllLocationList(queryString: String): NetworkResult<ArrayList<LocationSearchDataItem>>
    suspend fun fetchCurrentWeatherData(location: String): NetworkResult<CurrentWeatherData>
    suspend fun fetchCurrentWeatherDataInBulk(bulkDataRequest: BulkDataRequest): NetworkResult<FetchBulkData>
    fun fetchSavedCityListFromSharedPreferences(): List<LocationBulk>
    fun updateCityListToSharedPreferences(locationList: List<LocationBulk>)
}
