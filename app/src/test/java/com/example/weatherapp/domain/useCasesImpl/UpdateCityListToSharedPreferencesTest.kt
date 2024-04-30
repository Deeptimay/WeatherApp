package com.example.weatherapp.domain.useCasesImpl

import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UpdateCityListToSharedPreferencesTest {

    private lateinit var repository: WeatherAppRepository
    private lateinit var updateCityListToSharedPreferences: UpdateCityListToSharedPreferences

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        updateCityListToSharedPreferences = UpdateCityListToSharedPreferences(repository)
    }

    @Test
    fun `invoke calls updateCityListToSharedPreferences on the repository`() {
        val locationList = listOf(LocationBulk("New York"), LocationBulk("Los Angeles"))

        updateCityListToSharedPreferences(locationList)

        verify { repository.updateCityListToSharedPreferences(locationList) }
    }
}
