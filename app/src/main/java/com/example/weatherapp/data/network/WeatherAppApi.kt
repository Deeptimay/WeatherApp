package com.example.weatherapp.data.network

import com.example.weatherapp.data.models.BulkDataRequest
import com.example.weatherapp.data.models.CurrentWeatherData
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.data.models.LocationSearchData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherAppApi {

    @GET("/search.json")
    suspend fun locationAutoComplete(@Query("q") queryString: String): Response<LocationSearchData>

    @GET("/current.json")
    suspend fun getCurrentWeather(@Query("q") location: String): Response<CurrentWeatherData>

    @POST("/current.json")
    suspend fun getCurrentWeatherInBulk(@Body bulkDataRequest: BulkDataRequest, @Query("q") queryString: String = "bulk"): Response<FetchBulkData>
}
