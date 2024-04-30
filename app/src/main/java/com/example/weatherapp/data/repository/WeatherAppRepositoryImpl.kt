package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.data.network.WeatherAppApi
import com.example.weatherapp.data.sharedPreference.EncryptedSharedPreference
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import com.example.weatherapp.domain.util.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherAppRepositoryImpl @Inject constructor(
    private val baseRepository: BaseRepository,
    private val weatherAppApi: WeatherAppApi,
    private val encryptedSharedPreference: EncryptedSharedPreference
) : WeatherAppRepository {

    override suspend fun fetchAllLocationList(queryString: String): NetworkResult<ArrayList<LocationSearchDataItem>> {
        return baseRepository performApiCall {
            weatherAppApi.locationAutoComplete(queryString)
        }
    }

    override suspend fun fetchCurrentWeatherData(location: String): NetworkResult<CurrentWeatherData> {
        return baseRepository performApiCall {
            weatherAppApi.getCurrentWeather(location)
        }
    }

    override suspend fun fetchCurrentWeatherDataInBulk(bulkDataRequest: BulkDataRequest): NetworkResult<FetchBulkData> {
        return baseRepository performApiCall {
            weatherAppApi.getCurrentWeatherInBulk(bulkDataRequest)
        }
    }

    override fun fetchSavedCityListFromSharedPreferences(): List<LocationBulk> {
        return encryptedSharedPreference.retrieveMyPreferredLocations()
    }

    override fun updateCityListToSharedPreferences(locationList: List<LocationBulk>) {
        encryptedSharedPreference.saveMyPreferredLocations(locationList)
    }
}
