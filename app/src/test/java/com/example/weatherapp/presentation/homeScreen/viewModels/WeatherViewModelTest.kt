package com.example.weatherapp.presentation.homeScreen.viewModels


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.Current
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.Location
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.example.weatherapp.data.models.Query
import com.example.weatherapp.domain.useCasesImpl.FetchSavedCityListFromSharedPreferences
import com.example.weatherapp.domain.useCasesImpl.GetAllLocationSuggestions
import com.example.weatherapp.domain.useCasesImpl.GetCurrentWeatherInBulk
import com.example.weatherapp.domain.useCasesImpl.UpdateCityListToSharedPreferences
import com.example.weatherapp.domain.util.ErrorTypes
import com.example.weatherapp.domain.util.NetworkResult
import com.example.weatherapp.presentation.ui.UiState
import com.example.weatherapp.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: WeatherViewModel

    @Mock
    private var getAllLocationSuggestions: GetAllLocationSuggestions = mock()

    @Mock
    private var getCurrentWeatherInBulk: GetCurrentWeatherInBulk = mock()

    @Mock
    private var fetchSavedCityListFromSharedPreferences: FetchSavedCityListFromSharedPreferences = mock()

    @Mock
    private var updateCityListToSharedPreferences: UpdateCityListToSharedPreferences = mock()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = WeatherViewModel(
            getAllLocationSuggestions,
            getCurrentWeatherInBulk,
            fetchSavedCityListFromSharedPreferences,
            updateCityListToSharedPreferences
        )
    }

    @Test
    fun `fetchCurrentWeatherByCityInBulk success`() = runTest {
        `when`(getCurrentWeatherInBulk(bulkDataRequest)).thenReturn(fetchBulkDataNetworkResult)

        viewModel.fetchCurrentWeatherByCityInBulk(bulkDataRequest)
        delay(100)

        val result = viewModel.currentWeatherFlowInBulk.value
        assertEquals(UiState.Success(expectedResponseFetchBulkData), result)
    }

    @Test
    fun `getRepoList API error`() = runTest {
        `when`(getCurrentWeatherInBulk(bulkDataRequest)).thenReturn(fetchBulkDataNetworkResultError)

        viewModel.fetchCurrentWeatherByCityInBulk(bulkDataRequest)
        delay(100)

        // Then repoDetailsFlow should emit an Error state
        val result = viewModel.currentWeatherFlowInBulk.value
        assert(result is UiState.Error)
    }

    @Test
    fun `getRepoList API exception`() = runTest {
        `when`(getCurrentWeatherInBulk(bulkDataRequest)).thenReturn(fetchBulkDataNetworkResultException)

        viewModel.fetchCurrentWeatherByCityInBulk(bulkDataRequest)
        delay(100)

        val result = viewModel.currentWeatherFlowInBulk.value
        assert(result is UiState.Error)
    }

    @Test
    fun `fetchAllLocationSuggestions success`() = runTest {
        `when`(getAllLocationSuggestions(queryString)).thenReturn(locationSearchDataItemNetworkResult)

        viewModel.fetchAllLocationSuggestions(queryString)
        delay(100)

        val result = viewModel.locationFlow.value
        assertEquals(expectedResponseLocationSearchDataItemArraylist, result)
    }


    companion object {
        val queryString = "Bangalore"
        val expectedResponseCurrentWeatherData = CurrentWeatherData(current = Current(), location = Location(name = "Bangalore"))

        val locationSearchDataItem = listOf(LocationBulk(q = "Bangalore"))

        val expectedResponseLocationSearchDataItemArraylist = arrayListOf(LocationSearchDataItem(name = "Bangalore"))

        val locationList = listOf(LocationBulk(q = "Bangalore"))

        val bulkDataRequest = BulkDataRequest(locationList)
        val expectedResponseFetchBulkData = FetchBulkData(listOf(Bulk(Query(q = "Bangalore"))))

        val fetchBulkDataNetworkResult = NetworkResult.ApiSuccess(expectedResponseFetchBulkData)
        val locationSearchDataItemNetworkResult = NetworkResult.ApiSuccess(expectedResponseLocationSearchDataItemArraylist)

        val fetchBulkDataNetworkResultError = NetworkResult.ApiError<FetchBulkData>(
            ErrorTypes.ServerError(code = 400, internalMessage = "CustomError")

        )
        val fetchBulkDataNetworkResultException = NetworkResult.ApiError<FetchBulkData>(
            ErrorTypes.ExceptionError(Exception("CustomError"))
        )
    }
}
