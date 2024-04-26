package com.example.weatherapp.presentation.homeScreen


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val weatherViewModel = hiltViewModel<WeatherViewModel>()
    Column {
        TextView()
        SearchView(viewModel = weatherViewModel)
        CityListView(viewModel = weatherViewModel)
    }
}


@Composable
fun TextView() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        ) {
        Text(
            text = "Weather App",
            style = TextStyle(
                color = Color.Black,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                background = Color.White,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.padding(all = Dp(20.0F))
        )
    }
}
