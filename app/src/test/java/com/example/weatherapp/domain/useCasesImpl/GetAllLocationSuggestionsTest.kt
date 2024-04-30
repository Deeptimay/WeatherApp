package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import com.example.weatherapp.domain.util.NetworkResult
import org.junit.Assert.*

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetAllLocationSuggestionsTest {

    private lateinit var weatherAppRepository: WeatherAppRepository
    private lateinit var getAllLocationSuggestions: GetAllLocationSuggestions

    @Before
    fun setUp() {
        // Mock the WeatherAppRepository
        weatherAppRepository = mockk()
        // Initialize the use case with the mocked repository
        getAllLocationSuggestions = GetAllLocationSuggestions(weatherAppRepository)
    }

    @Test
    fun `invoke fetches location suggestions based on query string`() = runBlockingTest {
        // Setup the expected behavior and response
        val queryString = "New York"
        val expectedResponse = NetworkResult.ApiSuccess(arrayListOf(LocationSearchDataItem("New York")))
        coEvery { weatherAppRepository.fetchAllLocationList(queryString) } returns expectedResponse

        // Execute the use case
        val result = getAllLocationSuggestions(queryString)

        // Assert the expected result
        assertEquals(expectedResponse, result)
    }
}