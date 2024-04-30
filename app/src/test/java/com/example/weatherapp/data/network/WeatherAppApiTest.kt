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
import retrofit2.converter.gson.GsonConverterFactory
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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAppApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `locationAutoComplete returns expected data`() = runBlocking {

        mockWebServer.enqueue(mockResponseLocationAutoComplete)

        val response: Response<ArrayList<LocationSearchDataItem>> = api.locationAutoComplete("Bangalore")

        assertEquals(200, response.code())
        assertEquals("Bangalore", response.body()?.first()?.name)
    }

    @Test
    fun `getCurrentWeather returns expected data`() = runBlocking {

        mockWebServer.enqueue(mockResponseGetCurrentWeather)

        val response: Response<CurrentWeatherData> = api.getCurrentWeather("Bangalore")

        assertEquals(200, response.code())
        assertEquals("Bangalore", response.body()?.location?.name)
    }

    @Test
    fun `getCurrentWeatherInBulk returns expected data`() = runBlocking {

        mockWebServer.enqueue(mockResponseGetCurrentWeatherInBulk)

        val bulkDataRequest = BulkDataRequest(listOf(LocationBulk("Bangalore", "my_id_1")))
        val response: Response<FetchBulkData> = api.getCurrentWeatherInBulk(bulkDataRequest)

        assertEquals(200, response.code())
        assertEquals(1, response.body()?.bulk?.size)
    }

    companion object {
        val mockResponseGetCurrentWeatherInBulk = MockResponse()
            .setResponseCode(200)
            .setBody("{\"bulk\": [{\"query\": {\"custom_id\": \"my_id_1\", \"q\": \"bangalore\",\"location\":{\"name\":\"Bangalore\",\"region\":\"Karnataka\",\"country\":\"India\",\"lat\":12.98,\"lon\":77.58,\"tz_id\":\"Asia/Kolkata\",\"localtime_epoch\":1714457431,\"localtime\":\"2024-04-30 11:40\"},\"current\":{\"last_updated_epoch\":1714456800,\"last_updated\":\"2024-04-30 11:30\",\"temp_c\":34.0,\"temp_f\":93.2,\"is_day\":1,\"condition\":{\"text\":\"Sunny\",\"icon\":\"//cdn.weatherapi.com/weather/64x64/day/113.png\",\"code\":1000},\"wind_mph\":8.1,\"wind_kph\":13.0,\"wind_degree\":250,\"wind_dir\":\"WSW\",\"pressure_mb\":1015.0,\"pressure_in\":29.97,\"precip_mm\":0.0,\"precip_in\":0.0,\"humidity\":47,\"cloud\":0,\"feelslike_c\":32.8,\"feelslike_f\":91.1,\"vis_km\":8.0,\"vis_miles\":4.0,\"uv\":8.0,\"gust_mph\":12.5,\"gust_kph\":20.2}}}]}")

        val mockResponseGetCurrentWeather = MockResponse()
            .setResponseCode(200)
            .setBody("{\"location\":{\"name\":\"Bangalore\",\"region\":\"Karnataka\",\"country\":\"India\",\"lat\":12.98,\"lon\":77.58,\"tz_id\":\"Asia/Kolkata\",\"localtime_epoch\":1714459956,\"localtime\":\"2024-04-30 12:22\"},\"current\":{\"last_updated_epoch\":1714459500,\"last_updated\":\"2024-04-30 12:15\",\"temp_c\":36.0,\"temp_f\":96.8,\"is_day\":1,\"condition\":{\"text\":\"Partly cloudy\",\"icon\":\"//cdn.weatherapi.com/weather/64x64/day/116.png\",\"code\":1003},\"wind_mph\":2.2,\"wind_kph\":3.6,\"wind_degree\":106,\"wind_dir\":\"ESE\",\"pressure_mb\":1014.0,\"pressure_in\":29.94,\"precip_mm\":0.0,\"precip_in\":0.0,\"humidity\":37,\"cloud\":25,\"feelslike_c\":34.4,\"feelslike_f\":94.0,\"vis_km\":8.0,\"vis_miles\":4.0,\"uv\":9.0,\"gust_mph\":6.2,\"gust_kph\":9.9}}")

        val mockResponseLocationAutoComplete = MockResponse()
            .setResponseCode(200)
            .setBody("[{\"id\":1107187,\"name\":\"Bangalore\",\"region\":\"Karnataka\",\"country\":\"India\",\"lat\":12.98,\"lon\":77.58,\"url\":\"bangalore-karnataka-india\"}]")
    }
}