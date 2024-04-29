package com.example.weatherapp.presentation.homeScreen.composeViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.ui.theme.avenirFontFamily


@Composable
fun CardExtendedView(cityList: Bulk) {

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

    val precipitation = buildString {
        append(cityList.query.current.uv.toString())
        append(stringResource(R.string.mm))
    }
    val uvIndex = cityList.query.current.wind_kph.toString()
    val wind = buildString {
        append(cityList.query.current.wind_kph.toString())
        append(stringResource(R.string.kph))
    }
    val windDir = cityList.query.current.wind_dir
    val sun = stringResource(R.string._6_00_am)

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
                    fontFamily = avenirFontFamily
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
                    color = Color.Black,
                    fontFamily = avenirFontFamily
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
                        contentDescription = stringResource(R.string.icon),
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
                        text = stringResource(R.string._6_04_am),
                        style = TextStyle(
                            color = colorResource(id = R.color.text_color),
                            fontSize = 12.sp,
                        ),
                        textAlign = TextAlign.Center,
                        fontFamily = avenirFontFamily
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = stringResource(R.string.icon),
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
                        text = stringResource(R.string._6_49_pm),
                        style = TextStyle(
                            color = colorResource(id = R.color.text_color),
                            fontSize = 12.sp,
                        ),
                        textAlign = TextAlign.Center,
                        fontFamily = avenirFontFamily
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
                color = Color.Black,
                fontFamily = avenirFontFamily
            )
    }
}