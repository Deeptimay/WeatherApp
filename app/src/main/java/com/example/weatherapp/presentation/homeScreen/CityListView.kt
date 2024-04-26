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
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.presentation.ui.UiState
import com.example.weatherapp.presentation.util.convert24HourTo12Hour

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
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        itemsIndexed(items = weatherData) { index, cityList ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                elevation = 0.dp,
                onClick = { expanded = !expanded },
                content = {
                    CityListItem(cityList, expanded) { viewModel.removeSwipedWeatherByCityController(it) }
                }
            )
            ListDivider()
        }
    }
}

@Composable
fun CardContent(cityList: Bulk, expanded: Boolean) {
    Row(
        modifier = Modifier
            .padding(12.dp)
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
            val localTime = cityList.query.location.localtime.split(" ")
            val timeIn12Hour = convert24HourTo12Hour(localTime[1])
            Box(
                Modifier
                    .background(Color.White)
                    .height(70.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = cityList.query.location.name,
                    Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = cityList.query.location.region + ", " + cityList.query.location.country,
                    Modifier.align(Alignment.BottomStart)
                )
                Text(
                    text = timeIn12Hour,
                    Modifier.align(Alignment.BottomEnd)
                )

                Row(Modifier.align(Alignment.TopEnd)) {
                    Text(
                        text = cityList.query.current.temp_c.toString() + " C "
                    )
                    Image(
                        modifier = Modifier.size(width = 22.dp, height = 22.dp),
                        painter = painterResource(id = R.drawable.blue_clouds_and_yellow_sun_16529),
                        contentDescription = "My Drawable"
                    )
                }
            }

            if (expanded) {
                val precipitation = cityList.query.current.uv.toString() + " mm"
                val uvIndex = cityList.query.current.wind_kph.toString()
                val wind = cityList.query.current.wind_dir.toString() + " kph"
                val sun = "6:00 am"

                Row {
                    Column {
                        Card(
                            Modifier
                                .width(174.dp)
                                .height(74.dp)
                                .fillMaxWidth(),
                            elevation = 0.dp,
                            backgroundColor = colorResource(R.color.card_orange)

                        ) {
                            GridItem("Precipitation", precipitation)
                        }
                        Card(
                            Modifier
                                .width(174.dp)
                                .padding(8.dp)
                                .height(74.dp)
                                .fillMaxWidth(),
                            elevation = 0.dp,
                            backgroundColor = colorResource(R.color.card_orange)
                        ) {
                            GridItem("UV index", uvIndex)
                        }
                    }
                    Column {
                        Card(
                            Modifier
                                .width(174.dp)
                                .padding(8.dp)
                                .height(74.dp)
                                .fillMaxWidth(),
                            elevation = 0.dp,
                            backgroundColor = colorResource(R.color.card_orange)
                        ) {
                            GridItem("Wind", wind)
                        }
                        Card(
                            Modifier
                                .width(174.dp)
                                .padding(8.dp)
                                .height(74.dp)
                                .fillMaxWidth(),
                            elevation = 0.dp,
                            backgroundColor = colorResource(R.color.card_orange)
                        ) {
                            GridItem("Sun", sun)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridItem(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            text = title,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            text = value,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
fun ListDivider() {
    Divider(
        modifier = Modifier.padding(start = 14.dp),
        color = colorResource(id = R.color.grey_divider)
    )
}