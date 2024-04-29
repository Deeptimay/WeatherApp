package com.example.weatherapp.presentation.homeScreen.composeViews

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
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.presentation.util.convert24HourTo12Hour
import com.example.weatherapp.presentation.util.isDaytime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleItemCardContent(cityList: Bulk) {

    var expanded by remember { mutableStateOf(false) }
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
                R.drawable.day_cloudy
            else
                R.drawable.night_cloudy
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

    Card(
        elevation = 0.dp,
        backgroundColor = Color.White,
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessHigh
                )
            ),
        onClick = { expanded = !expanded },
    ) {
        Column {
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
                        .padding(top = 30.dp, start = 12.dp),
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
                        .padding(top = 30.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildString {
                            append(cityList.query.current.temp_c.toString())
                            append(stringResource(R.string.celsius))
                            append(stringResource(R.string.c))
                        },
                        fontSize = 24.sp,
                    )
                    Image(
                        modifier = Modifier
                            .size(width = 22.dp, height = 22.dp)
                            .padding(start = 5.dp),
                        painter = painterResource(id = id),
                        contentDescription = stringResource(R.string.weather_condition)
                    )
                }
            }
            if (expanded) {
                CardExtendedView(cityList)
            }
            ListDivider()
        }
    }
}

@Composable
fun ListDivider() {
    Divider(
        modifier = Modifier.padding(start = 12.dp),
        color = colorResource(id = R.color.grey_divider)
    )
}