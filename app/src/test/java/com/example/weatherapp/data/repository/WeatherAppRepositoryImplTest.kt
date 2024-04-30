package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.Current
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.Location
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.data.models.Query
import com.example.weatherapp.data.network.WeatherAppApi
import com.example.weatherapp.data.sharedPreference.EncryptedSharedPreference
import com.example.weatherapp.domain.util.ErrorTypes
import com.example.weatherapp.domain.util.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class WeatherAppRepositoryImplTest {

    private lateinit var baseRepository: BaseRepository
    private lateinit var weatherAppApi: WeatherAppApi
    private lateinit var encryptedSharedPreference: EncryptedSharedPreference
    private lateinit var repository: WeatherAppRepositoryImpl

    @Before
    fun setUp() {
        baseRepository = mockk(relaxed = true)
        weatherAppApi = mockk(relaxed = true)
        encryptedSharedPreference = mockk(relaxed = true)
        repository = WeatherAppRepositoryImpl(baseRepository, weatherAppApi, encryptedSharedPreference)
    }

    @Test
    fun `fetchAllLocationList calls API and returns result`() = runBlockingTest {

        coEvery { weatherAppApi.locationAutoComplete(queryString) } returns Response.success(expectedResponseLocationSearchDataItemArraylist)
        coEvery { baseRepository.performApiCall<ArrayList<LocationSearchDataItem>>(any()) } coAnswers {
            val apiCall = firstArg<suspend () -> Response<ArrayList<LocationSearchDataItem>>>()
            val response = apiCall.invoke()
            if (response.isSuccessful) NetworkResult.ApiSuccess(response.body()!!) else NetworkResult.ApiError(
                ErrorTypes.CustomError(code = response.code(), internalMessage = response.message())
            )
        }

        val result = repository.fetchAllLocationList(queryString)

        assertEquals(expectedResponseLocationSearchDataItemArraylist, (result as NetworkResult.ApiSuccess<*>).data)
        coVerify(exactly = 1) { weatherAppApi.locationAutoComplete(queryString) }
    }

    @Test
    fun `fetchCurrentWeatherData calls API and returns result`() = runBlockingTest {

        coEvery { weatherAppApi.getCurrentWeather(queryString) } returns Response.success(expectedResponseCurrentWeatherData)
        coEvery { baseRepository.performApiCall<CurrentWeatherData>(any()) } coAnswers {
            val apiCall = firstArg<suspend () -> Response<CurrentWeatherData>>()
            val response = apiCall.invoke()
            if (response.isSuccessful) NetworkResult.ApiSuccess(response.body()!!) else NetworkResult.ApiError(
                ErrorTypes.CustomError(code = response.code(), internalMessage = response.message())
            )
        }

        val result = repository.fetchCurrentWeatherData(queryString)

        assertEquals(expectedResponseCurrentWeatherData, (result as NetworkResult.ApiSuccess<*>).data)
        coVerify(exactly = 1) { weatherAppApi.getCurrentWeather(queryString) }
    }

    @Test
    fun `fetchCurrentWeatherDataInBulk calls API and returns result`() = runBlockingTest {

        coEvery { weatherAppApi.getCurrentWeatherInBulk(bulkDataRequest) } returns Response.success(expectedResponseFetchBulkData)
        coEvery { baseRepository.performApiCall<FetchBulkData>(any()) } coAnswers {
            val apiCall = firstArg<suspend () -> Response<FetchBulkData>>()
            val response = apiCall.invoke()
            if (response.isSuccessful) NetworkResult.ApiSuccess(response.body()!!) else NetworkResult.ApiError(
                ErrorTypes.CustomError(code = response.code(), internalMessage = response.message())
            )
        }

        val result = repository.fetchCurrentWeatherDataInBulk(bulkDataRequest)

        assertEquals(expectedResponseFetchBulkData, (result as NetworkResult.ApiSuccess<*>).data)
        coVerify(exactly = 1) { weatherAppApi.getCurrentWeatherInBulk(bulkDataRequest) }
    }

    @Test
    fun `fetchSavedCityListFromSharedPreferences retrieves data correctly`() {
        val expectedList = listOf(LocationBulk("New York"))
        coEvery { encryptedSharedPreference.retrieveMyPreferredLocations() } returns expectedList

        val result = repository.fetchSavedCityListFromSharedPreferences()

        assertEquals(expectedList, result)
    }

    @Test
    fun `updateCityListToSharedPreferences saves data correctly`() {
        val locationList = listOf(LocationBulk("San Francisco"))

        repository.updateCityListToSharedPreferences(locationList)

        coVerify { encryptedSharedPreference.saveMyPreferredLocations(locationList) }
    }

    companion object {
        val queryString = "Bangalore"
        val expectedResponseCurrentWeatherData = CurrentWeatherData(current = Current(), location = Location(name = "Bangalore"))

        val expectedResponseLocationSearchDataItemArraylist = arrayListOf(LocationSearchDataItem(name = "Bangalore"))

        val bulkDataRequest = BulkDataRequest(listOf(LocationBulk(q = "Bangalore")))
        val expectedResponseFetchBulkData = FetchBulkData(listOf(Bulk(Query(q = "Bangalore"))))
    }
}