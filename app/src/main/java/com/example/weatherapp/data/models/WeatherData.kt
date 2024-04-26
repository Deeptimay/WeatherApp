package com.example.weatherapp.data.models

class LocationSearchData : ArrayList<LocationSearchDataItem>()
data class BulkDataRequest(
    val locations: List<LocationBulk>
)

data class LocationBulk(
    val q: String,
    val custom_id: String
)

data class FetchBulkData(val bulk: List<Bulk>)

data class LocationSearchDataItem(
    val country: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val region: String,
    val url: String
)

data class Bulk(
    val query: Query
)

data class Query(
    val current: Current,
    val custom_id: String,
    val location: Location,
    val q: String
)

data class Current(
    val cloud: Int,
    val condition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val gust_kph: Double,
    val gust_mph: Double,
    val humidity: Int,
    val is_day: Int,
    val last_updated: String,
    val last_updated_epoch: Int,
    val precip_in: Double,
    val precip_mm: Double,
    val pressure_in: Double,
    val pressure_mb: Double,
    val temp_c: Double,
    val temp_f: Double,
    val uv: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_kph: Double,
    val wind_mph: Double
)

data class Location(
    val country: String = "",
    val lat: Double = 0.0,
    val localtime: String = "",
    val localtime_epoch: Int = 0,
    val lon: Double = 0.0,
    var name: String = "",
    val region: String = "",
    val tz_id: String = ""
)

data class Condition(
    val code: Int,
    val icon: String,
    val text: String
)

data class CurrentWeatherData(
    val current: Current,
    val location: Location
)