package com.example.weatherapp.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.LocationSearchDataItem

@Composable
fun SearchView(viewModel: WeatherViewModel) {
    val searchResults by viewModel.locationFlow.collectAsState()

    SearchScreen(
        searchQuery = viewModel.searchQuery,
        searchResults = searchResults,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        onSearchClicked = {
            viewModel.fetchCurrentWeatherByCityController(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<LocationSearchDataItem>,
    onSearchQueryChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        SearchBar(
            modifier = Modifier
                .background(Color.White)
                .height(100.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = { keyboardController?.hide() },
            placeholder = {
                Text(text = "City, Region or US/UK zip code")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            active = true,
            onActiveChange = {},
            tonalElevation = 0.dp
        ) {}
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .background(Color.White)
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                items(
                    count = searchResults.size,
                    key = { index -> searchResults[index].id },
                    itemContent = { index ->
                        val location = searchResults[index]
                        LocationListItem(location = location, onItemSelected = {
                            onSearchClicked(it)
                            onSearchQueryChange("")
                        })
                        Divider(
                            color = colorResource(R.color.grey_divider), modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .width(1.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun LocationListItem(
    location: LocationSearchDataItem,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(Color.White)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onItemSelected(location.name) }
    ) {
        Text(text = location.name + ", " + location.region + ", " + location.country)
    }
}