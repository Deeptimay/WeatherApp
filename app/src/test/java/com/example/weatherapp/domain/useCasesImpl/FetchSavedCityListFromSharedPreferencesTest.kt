package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

class FetchSavedCityListFromSharedPreferencesTest {

    private lateinit var weatherAppRepository: WeatherAppRepository
    private lateinit var fetchSavedCityListFromSharedPreferences: FetchSavedCityListFromSharedPreferences

    @Before
    fun setUp() {
        // Create a mock instance of WeatherAppRepository
        weatherAppRepository = mock(WeatherAppRepository::class.java)
        // Initialize the use case with the mocked repository
        fetchSavedCityListFromSharedPreferences = FetchSavedCityListFromSharedPreferences(weatherAppRepository)
    }

    @Test
    fun `invoke calls fetchSavedCityListFromSharedPreferences on the repository`() {
        // Prepare the mock repository to return a predefined list of locations
        val expectedList = listOf(LocationBulk("New York"), LocationBulk("Los Angeles"))
        `when`(weatherAppRepository.fetchSavedCityListFromSharedPreferences()).thenReturn(expectedList)

        // Call the use case
        val resultList = fetchSavedCityListFromSharedPreferences.invoke()

        // Verify that the repository method was called
        verify(weatherAppRepository).fetchSavedCityListFromSharedPreferences()
        // Assert that the returned list from the use case is the expected one
        assertEquals(expectedList, resultList)
    }
}