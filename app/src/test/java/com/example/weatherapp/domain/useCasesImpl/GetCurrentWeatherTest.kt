package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.Location
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
class GetCurrentWeatherTest {

    private lateinit var weatherAppRepository: WeatherAppRepository
    private lateinit var getCurrentWeather: GetCurrentWeather

    @Before
    fun setUp() {
        // Create a mock instance of WeatherAppRepository
        weatherAppRepository = mockk(relaxed = true)
        // Initialize the use case with the mocked repository
        getCurrentWeather = GetCurrentWeather(weatherAppRepository)
    }

    @Test
    fun `invoke fetches current weather data based on location`() = runBlockingTest {
        // Setup the expected behavior and response
        val location = "San Francisco"
        val expectedResponse = NetworkResult.ApiSuccess(CurrentWeatherData(location = Location(country = location)))
        coEvery { weatherAppRepository.fetchCurrentWeatherData(location) } returns expectedResponse

        // Execute the use case
        val result = getCurrentWeather(location)

        // Verify the result matches the expected outcome
        assertEquals(expectedResponse, result)
    }
}
