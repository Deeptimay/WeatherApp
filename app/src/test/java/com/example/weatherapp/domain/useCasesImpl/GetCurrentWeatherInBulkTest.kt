package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import com.example.weatherapp.domain.util.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetCurrentWeatherInBulkTest {

    private lateinit var weatherAppRepository: WeatherAppRepository
    private lateinit var getCurrentWeatherInBulk: GetCurrentWeatherInBulk

    @Before
    fun setUp() {
        // Create a mock instance of WeatherAppRepository
        weatherAppRepository = mockk()
        // Initialize the use case with the mocked repository
        getCurrentWeatherInBulk = GetCurrentWeatherInBulk(weatherAppRepository)
    }

    @Test
    fun `invoke fetches bulk weather data based on bulk request`() = runBlockingTest {
        // Setup the expected behavior and response
        val bulkDataRequest = BulkDataRequest(listOf(LocationBulk()))
        val expectedResponse = NetworkResult.ApiSuccess(FetchBulkData(listOf(Bulk())))
        coEvery { weatherAppRepository.fetchCurrentWeatherDataInBulk(bulkDataRequest) } returns expectedResponse

        // Execute the use case
        val result = getCurrentWeatherInBulk(bulkDataRequest)

        // Verify the result matches the expected outcome
        assertEquals(expectedResponse, result)
    }
}
