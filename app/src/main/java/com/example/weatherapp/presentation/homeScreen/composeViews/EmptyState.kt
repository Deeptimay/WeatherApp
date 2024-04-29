package com.example.weatherapp.presentation.homeScreen.composeViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R

@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Icon(
            modifier = Modifier
                .size(width = 86.dp, height = 88.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.weather_mix),
            contentDescription = stringResource(R.string.my_drawable),
            tint = colorResource(id = R.color.empty_screen)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .align(Alignment.CenterHorizontally),
            text = message,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.search_icons)
        )
    }
}