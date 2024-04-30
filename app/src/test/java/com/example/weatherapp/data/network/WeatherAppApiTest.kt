package com.example.weatherapp.data.network


import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationBulk
import com.example.weatherapp.data.models.LocationSearchDataItem
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertEquals

class WeatherAppApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: WeatherAppApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder().build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherAppApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `locationAutoComplete returns expected data`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[{\"name\":\"New York\"}]")
        mockWebServer.enqueue(mockResponse)

        val response: Response<ArrayList<LocationSearchDataItem>> = api.locationAutoComplete("New York")

        assertEquals(200, response.code())
        assertEquals("New York", response.body()?.first()?.name)
    }

    @Test
    fun `getCurrentWeather returns expected data`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"temperature\":23}")
        mockWebServer.enqueue(mockResponse)

        val response: Response<CurrentWeatherData> = api.getCurrentWeather("12345")

        assertEquals(200, response.code())
//        assertEquals(23, response.body()?.current)
    }

    @Test
    fun `getCurrentWeatherInBulk returns expected data`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"data\":[{\"temperature\":23},{\"temperature\":25}]}")
        mockWebServer.enqueue(mockResponse)

        val bulkDataRequest = BulkDataRequest(listOf(LocationBulk()))
        val response: Response<FetchBulkData> = api.getCurrentWeatherInBulk(bulkDataRequest)

        assertEquals(200, response.code())
        assertEquals(2, response.body()?.bulk?.size)
    }

    companion object{

    }
}