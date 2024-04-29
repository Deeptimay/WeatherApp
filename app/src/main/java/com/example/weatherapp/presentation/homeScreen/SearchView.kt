package com.example.weatherapp.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    var active by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth()
    ) {
        DockedSearchBar(
            modifier = Modifier
                .wrapContentHeight()
                .background(colorResource(id = R.color.search_bar), RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            colors = SearchBarDefaults.colors(containerColor = colorResource(id = R.color.search_bar), dividerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp),
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            active = active,
            onSearch = { keyboardController?.hide() },
            placeholder = {
                Text(text = stringResource(R.string.city_region_or_us_uk_zip_code), color = colorResource(id = R.color.search_icons))
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = stringResource(R.string.search_icon),
                    tint = colorResource(id = R.color.search_icons)
                )
            },
            trailingIcon = {
                if (active) {
                    IconButton(onClick = {
                        onSearchQueryChange("")
                        active = false
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = stringResource(R.string.clear_search),
                            tint = colorResource(id = R.color.search_icons)
                        )
                    }
                }
            },
            onActiveChange = { active = it }
        ) {
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