package com.example.weatherapp.presentation.homeScreen.composeViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Bulk
import com.example.weatherapp.data.models.FetchBulkData
import com.example.weatherapp.presentation.homeScreen.viewModels.WeatherViewModel
import com.example.weatherapp.presentation.ui.UiState

@Composable
fun CityListView(viewModel: WeatherViewModel) {
    val responseData by viewModel.currentWeatherFlowInBulk.collectAsState()

    when (responseData) {
        is UiState.Error -> {
            val errorData = responseData as? UiState.Error

            if (errorData.toString().contains(stringResource(R.string.unknownhostexception))) {
                EmptyState(stringResource(R.string.please_check_your_internet))
                return
            }

            if (!viewModel.currentWeatherList.isNullOrEmpty())
                EmptyState(errorData.toString())
            else
                EmptyState(stringResource(R.string.search_for_a_city_or_us_uk_zip_to_check_the_weather))
        }

        UiState.Loading -> {
            EmptyState(stringResource(R.string.loading_your_data))
        }

        is UiState.Success<*> -> {
            val successData = responseData as? UiState.Success<*>
            val weatherCityListData = successData?.content as? FetchBulkData
            if (weatherCityListData != null && !weatherCityListData.bulk.isNullOrEmpty()) {
                weatherCityListData?.let { CityList(it.bulk, viewModel) }
            } else {
                EmptyState(stringResource(R.string.search_for_a_city_or_us_uk_zip_to_check_the_weather))
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
        itemsIndexed(items = weatherData) { _, cityList ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                elevation = 0.dp,
                backgroundColor = Color.White,
                onClick = { expanded = !expanded },
                content = {
                    CityListItemWrapper(cityList, expanded) { viewModel.removeSwipedWeatherByCityController(it) }
                }
            )
        }
    }
}
