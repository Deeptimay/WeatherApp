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
        is UiState.Error -> {}
        UiState.Loading -> {}
        is UiState.Success<*> -> {
            val successData = responseData as? UiState.Success<*>
            val weatherCityListData = successData?.content as? FetchBulkData
            weatherCityListData?.let { CityList(it.bulk, viewModel) }
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
                onClick = { expanded = !expanded }
            ) {
                CityListItem(cityList, expanded) { viewModel.removeSwipedWeatherByCityController(it) }
            }
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
                            backgroundColor = colorResource(R.color.card_orange)
                        ) {
                            GridItem("Sun", sun)
                        }
                    }
                }
            }
//                androidx.compose.material.Text(text = ("Lorem ipsum composium is so cool!!!!!\t").repeat(3))
        }
    }
//        androidx.compose.material.IconButton(onClick = { expanded = !expanded }) {
//            Icon(
//                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
//                contentDescription = if (expanded) {
//                    stringResource(id = R.string.show_less)
//                } else {
//                    stringResource(id = R.string.show_more)
//                }
//            )
//        }
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
//
//@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
//@Composable
//fun gridView() {
//
//    lateinit var courseList: List<GridModal>
//    courseList = ArrayList<GridModal>()
//
//    // on below line we are adding data to our list.
//    courseList = courseList + GridModal("Android", R.drawable.android)
//    courseList = courseList + GridModal("JavaScript", R.drawable.js)
//    courseList = courseList + GridModal("Python", R.drawable.python)
//    courseList = courseList + GridModal("C++", R.drawable.c)
//    courseList = courseList + GridModal("C#", R.drawable.csharp)
//    courseList = courseList + GridModal("Java", R.drawable.java)
//    courseList = courseList + GridModal("Node Js", R.drawable.nodejs)
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.padding(10.dp)
//    ) {
//        items(courseList.size) {
//            Card(
//                onClick = {},
//                modifier = Modifier.padding(8.dp),
////                elevation = 6.dp
//            ) {
//                Column(
//                    Modifier
//                        .fillMaxSize()
//                        .padding(5.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = courseList[it].languageImg),
//                        contentDescription = "Javascript",
//                        modifier = Modifier
//                            .height(60.dp)
//                            .width(60.dp)
//                            .padding(5.dp)
//                    )
//                    Spacer(modifier = Modifier.height(9.dp))
//                    Text(
//                        text = courseList[it].languageName,
//                        modifier = Modifier.padding(4.dp),
//                        color = Color.Black
//                    )
//                }
//            }
//        }
//    }
//}