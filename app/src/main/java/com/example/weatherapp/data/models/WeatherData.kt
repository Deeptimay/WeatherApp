package com.example.weatherapp.data.models

data class BulkDataRequest(
    val locations: List<LocationBulk> = listOf()
)

data class LocationBulk(
    val q: String = "",
    val custom_id: String = ""
)

data class FetchBulkData(val bulk: List<Bulk>)

data class LocationSearchDataItem(
    val country: String = "",
    val id: Int = 0,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val name: String = "",
    val region: String = "",
    val url: String = ""
)

data class Bulk(
    val query: Query = Query()
)

data class Query(
    val current: Current = Current(),
    val custom_id: String = "",
    val location: Location = Location(),
    val q: String = ""
)

data class Current(
    val cloud: Int = 0,
    val condition: Condition = Condition(),
    val feelslike_c: Double = 0.0,
    val feelslike_f: Double = 0.0,
    val gust_kph: Double = 0.0,
    val gust_mph: Double = 0.0,
    val humidity: Int = 0,
    val is_day: Int = 0,
    val last_updated: String = "",
    val last_updated_epoch: Int = 0,
    val precip_in: Double = 0.0,
    val precip_mm: Double = 0.0,
    val pressure_in: Double = 0.0,
    val pressure_mb: Double = 0.0,
    val temp_c: Double = 0.0,
    val temp_f: Double = 0.0,
    val uv: Double = 0.0,
    val vis_km: Double = 0.0,
    val vis_miles: Double = 0.0,
    val wind_degree: Int = 0,
    val wind_dir: String = "",
    val wind_kph: Double = 0.0,
    val wind_mph: Double = 0.0
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
    val code: Int = 0,
    val icon: String = "",
    val text: String = ""
)

data class CurrentWeatherData(
    val current: Current = Current(),
    val location: Location = Location()
)