package com.example.weatherapp.presentation.homeScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.presentation.ui.UiState
import com.example.weatherapp.presentation.util.convert24HourTo12Hour
import com.example.weatherapp.presentation.util.isDaytime

@Composable
fun CityListView(viewModel: WeatherViewModel) {
    val responseData by viewModel.currentWeatherFlowInBulk.collectAsState()

    when (responseData) {
        is UiState.Error -> {
            val errorData = responseData as? UiState.Error
            if (!viewModel.currentWeatherList.isNullOrEmpty())
                EmptyState(errorData.toString())
            else
                EmptyState("Search for a city or US/UK zip to check the weather")
        }

        UiState.Loading -> {
            EmptyState("Loading Your Data")
        }

        is UiState.Success<*> -> {
            val successData = responseData as? UiState.Success<*>
            val weatherCityListData = successData?.content as? FetchBulkData
            if (weatherCityListData != null && !weatherCityListData.bulk.isNullOrEmpty()) {
                weatherCityListData?.let { CityList(it.bulk, viewModel) }
            } else {
                EmptyState("Search for a city or US/UK zip to check the weather")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CityList(weatherData: List<Bulk>, viewModel: WeatherViewModel) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(color = Color.White)
    ) {
        itemsIndexed(items = weatherData) { index, cityList ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                elevation = 0.dp,
                backgroundColor = Color.White,
                onClick = { expanded = !expanded },
                content = {
                    CityListItem(cityList, expanded) { viewModel.removeSwipedWeatherByCityController(it) }
                }
            )

        }
    }
}

@Composable
fun CardContent(cityList: Bulk, expanded: Boolean) {

    val localTime = cityList.query.location.localtime.split(" ")
    val timeIn12Hour = convert24HourTo12Hour(localTime[1])
    val isDay = isDaytime(localTime[1])

    val id = when {
        cityList.query.current.condition.text.contains("Sunny", ignoreCase = true) -> {
            if (isDay)
                R.drawable.day
            else
                R.drawable.night
        }

        cityList.query.current.condition.text.contains("rain", ignoreCase = true) -> {
            if (isDay)
                R.drawable.day_rain
            else
                R.drawable.night_rain
        }

        cityList.query.current.condition.text.contains("Thunder", ignoreCase = true) -> {
            if (isDay)
                R.drawable.day_thunder
            else
                R.drawable.night_thunder
        }

        cityList.query.current.condition.text.contains("snow", ignoreCase = true) ||
                cityList.query.current.condition.text.contains("Ice", ignoreCase = true) -> {
            if (isDay)
                R.drawable.day_snow
            else
                R.drawable.night_snow
        }

        cityList.query.current.condition.text.contains("Cloudy", ignoreCase = true) ||
                cityList.query.current.condition.text.contains("Mist", ignoreCase = true) -> {
            if (isDay)
                R.drawable.night_cloudy
            else
                R.drawable.day_cloudy
        }

        cityList.query.current.condition.text.contains("showers", ignoreCase = true) -> {
            if (isDay)
                R.drawable.day_rain
            else
                R.drawable.night_rain
        }

        else -> if (isDay)
            R.drawable.day
        else
            R.drawable.night
    }

    Row(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessHigh
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                Modifier
                    .background(Color.White)
                    .height(105.dp)
                    .fillMaxWidth()

            ) {

                Text(
                    text = cityList.query.location.name,
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 35.dp, start = 12.dp),
                    fontSize = 24.sp,
                )
                Text(
                    text = cityList.query.location.region + ", " + cityList.query.location.country,
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 12.dp, start = 12.dp),
                    fontSize = 14.sp,
                )
                Text(
                    text = timeIn12Hour,
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 12.dp, end = 12.dp),
                    fontSize = 14.sp,
                )

                Row(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 35.dp, end = 12.dp)
                ) {
                    Text(
                        text = cityList.query.current.temp_c.toString() + " c ",
                        fontSize = 24.sp,
                    )
                    Image(
                        modifier = Modifier.size(width = 22.dp, height = 22.dp),
                        painter = painterResource(id = id),
                        contentDescription = "Weather Condition"
                    )
                }
            }

            if (expanded) {
                ExtendedItemNew(cityList)
            }
            ListDivider()
        }
    }
}

@Composable
fun ExtendedItemNew(cityList: Bulk) {

    Row(
        Modifier
            .background(color = Color.White)
            .padding(6.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(75.dp),
                elevation = 0.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = colorResource(R.color.card_orange)

            ) {
                GridItem("Precipitation", cityList)
            }
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(75.dp),
                elevation = 0.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = colorResource(R.color.card_orange)
            ) {
                GridItem("UV index", cityList)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(75.dp),
                elevation = 0.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = colorResource(R.color.card_orange)
            ) {
                GridItem("Wind", cityList)
            }
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(75.dp),
                elevation = 0.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = colorResource(R.color.card_orange)
            ) {
                GridItem("Sun", cityList)
            }
        }
    }
}

@Composable
fun GridItem(title: String, cityList: Bulk) {

    val precipitation = cityList.query.current.uv.toString() + " mm"
    val uvIndex = cityList.query.current.wind_kph.toString()
    val wind = cityList.query.current.wind_kph.toString() + " kph"
    val windDir = cityList.query.current.wind_dir
    val sun = "6:00 am"

    var textValue = ""
    val id = when (title) {
        "Precipitation" -> {
            textValue = precipitation
            R.drawable.precipitation
        }

        "UV index" -> {
            textValue = uvIndex
            R.drawable.wb_sunny
        }

        "Wind" -> {
            textValue = wind
            R.drawable.air
        }

        "Sun" -> {
            textValue = sun
            R.drawable.sunny
        }

        else -> {
            textValue = ""
            R.drawable.close
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = id),
                    contentDescription = "icon",
                    Modifier
                        .padding(start = 5.dp)
                        .height(14.dp)
                        .width(14.dp),
                    tint = colorResource(id = R.color.text_color)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    text = title,
                    style = TextStyle(
                        color = colorResource(id = R.color.text_color),
                        fontSize = 12.sp,
                    ),
                    textAlign = TextAlign.Center,
                )
            }
            if (!title.equals("Sun", ignoreCase = true)) {
                Text(
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    text = textValue,
                    style = TextStyle(
                        color = colorResource(id = R.color.text_color_two),
                        fontSize = 16.sp,
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 5.dp)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sunrise),
                        contentDescription = "icon",
                        Modifier
                            .height(14.dp)
                            .width(14.dp)
                            .padding(end = 5.dp),
                        tint = colorResource(id = R.color.text_color)
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        text = "6:04 am",
                        style = TextStyle(
                            color = colorResource(id = R.color.text_color),
                            fontSize = 12.sp,
                        ),
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = "icon",
                        Modifier
                            .padding(start = 5.dp, end = 5.dp)
                            .height(14.dp)
                            .width(14.dp),
                        tint = colorResource(id = R.color.text_color)
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        text = "6:49 pm",
                        style = TextStyle(
                            color = colorResource(id = R.color.text_color),
                            fontSize = 12.sp,
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        if (title.equals("Wind", ignoreCase = true))
            Text(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .wrapContentWidth()
                    .align(Alignment.TopEnd)
                    .wrapContentHeight(),
                text = windDir,
                style = TextStyle(
                    color = colorResource(id = R.color.text_color_two),
                    fontSize = 30.sp
                ),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = Color.Black
            )
    }
}

@Composable
fun ListDivider() {
    Divider(
        modifier = Modifier.padding(start = 12.dp),
        color = colorResource(id = R.color.grey_divider)
    )
}