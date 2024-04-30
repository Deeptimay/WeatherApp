package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.Current
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.Location
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.data.network.WeatherAppApi
import com.example.weatherapp.data.sharedPreference.EncryptedSharedPreference
import com.example.weatherapp.domain.util.NetworkResult
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class WeatherAppRepositoryImplTest {

    @Mock
    private lateinit var baseRepository: BaseRepository

    @Mock
    private lateinit var weatherAppApi: WeatherAppApi

    @Mock
    private lateinit var encryptedSharedPreference: EncryptedSharedPreference


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = WeatherAppRepositoryImpl(baseRepository, weatherAppApi, encryptedSharedPreference)
    }

    @Test
    fun `fetchAllLocationList returns locations on successful API call`() = runBlocking {

        whenever(baseRepository.performApiCall<ArrayList<LocationSearchDataItem>>(any())).thenReturn(expectedResultLocationSearch)

        val result = repository.fetchAllLocationList(queryString)

        verify(weatherAppApi).locationAutoComplete(queryString)
        assertEquals(expectedResultLocationSearch, result)
    }

    @Test
    fun `fetchCurrentWeatherData returns weather data on successful API call`() = runBlocking {

        whenever(baseRepository.performApiCall<CurrentWeatherData>(any())).thenReturn(expectedResult)

        val result = repository.fetchCurrentWeatherData(location)

        verify(weatherAppApi).getCurrentWeather(location)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `fetchCurrentWeatherDataInBulk returns bulk weather data on successful API call`() = runBlocking {

        whenever(baseRepository.performApiCall<FetchBulkData>(any())).thenReturn(expectedResultBulk)

        val result = repository.fetchCurrentWeatherDataInBulk(bulkRequest)

        verify(weatherAppApi).getCurrentWeatherInBulk(bulkRequest)
        assertEquals(expectedResultBulk, result)
    }

    @Test
    fun `fetchSavedCityListFromSharedPreferences returns list of saved cities`() {
        whenever(encryptedSharedPreference.retrieveMyPreferredLocations()).thenReturn(expectedList)

        val result = repository.fetchSavedCityListFromSharedPreferences()

        assertEquals(expectedList, result)
    }

    @Test
    fun `updateCityListToSharedPreferences saves list of cities`() {

        repository.updateCityListToSharedPreferences(cityList)

        verify(encryptedSharedPreference).saveMyPreferredLocations(cityList)
    }

    companion object {

        private lateinit var repository: WeatherAppRepositoryImpl

        val location = "Bangalore"
        val expectedResult = NetworkResult.ApiSuccess(CurrentWeatherData(current = Current(), location = Location()))
        val bulkRequest = BulkDataRequest(listOf(LocationBulk()))
        val expectedResultBulk = NetworkResult.ApiSuccess(FetchBulkData(listOf()))
        val expectedList = listOf(LocationBulk("Bangalore"), LocationBulk("Bangalore"))

        val cityList = listOf(LocationBulk("Bangalore"), LocationBulk("Bangalore"))
        val queryString = "Bangalore"
        val expectedResultLocationSearch = NetworkResult.ApiSuccess(ArrayList<LocationSearchDataItem>(listOf()))
    }
}