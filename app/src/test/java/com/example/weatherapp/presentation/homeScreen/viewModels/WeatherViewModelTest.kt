package com.example.weatherapp.presentation.homeScreen.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.domain.useCasesImpl.FetchSavedCityListFromSharedPreferences
import com.example.weatherapp.domain.useCasesImpl.GetAllLocationSuggestions
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeatherInBulk
import com.example.weatherapp.domain.useCasesImpl.UpdateCityListToSharedPreferences
import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.presentation.ui.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel
    private val getAllLocationSuggestions: GetAllLocationSuggestions = mockk()
    private val getCurrentWeatherInBulk: GetCurrentWeatherInBulk = mockk()
    private val fetchSavedCityListFromSharedPreferences: FetchSavedCityListFromSharedPreferences = mockk()
    private val updateCityListToSharedPreferences: UpdateCityListToSharedPreferences = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)  // Set the main dispatcher to unconfined for testing

        // Initialize the ViewModel with mocked use cases
        viewModel = WeatherViewModel(
            getAllLocationSuggestions,
            getCurrentWeatherInBulk,
            fetchSavedCityListFromSharedPreferences,
            updateCityListToSharedPreferences
        )
    }

    @Test
    fun `fetchAllLocationWeatherInBulk updates currentWeatherFlowInBulk with success when list is not empty`(): Nothing = runBlocking {
        val mockList = listOf(LocationBulk("New York"))
        coEvery { fetchSavedCityListFromSharedPreferences.invoke() } returns mockList
        coEvery { getCurrentWeatherInBulk.invoke(any()) } returns NetworkResult.ApiSuccess(FetchBulkData(listOf(Bulk())))

        viewModel.currentWeatherFlowInBulk.collect { state ->
            when (state) {
                is UiState.Success<*> -> assertEquals(FetchBulkData(listOf(Bulk())), state.content as? FetchBulkData)
                is UiState.Error -> assert(false)  // Should not reach here
                UiState.Loading -> {}
            }
        }
    }

    @Test
    fun `fetchAllLocationSuggestions updates locationFlow on valid query`() = runBlocking {
        val mockResult = arrayListOf(LocationSearchDataItem("New York"))
        coEvery { getAllLocationSuggestions.invoke("NY") } returns NetworkResult.ApiSuccess(mockResult)

        viewModel.onSearchQueryChange("NY")

        assertEquals(mockResult, viewModel.locationFlow.first())
    }
}
