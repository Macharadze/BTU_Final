package com.example.project_work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project_work.images.WeatherIcon

@Composable
fun DetailScreen(date: String, viewModel: WeatherModel) {
    val forecast = viewModel.weatherForecast.value.find { it.date == date }

    forecast?.let {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center,
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(30.dp)
            ) {
                Text(viewModel.currentCity, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                Text(viewModel.formatDateWithWeekday(it.date), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 20.dp))
                // Using Coil for efficient image loading
                WeatherIcon(
                    iconId = forecast.iconId,
                    modifier = Modifier.size(100.dp, 100.dp),
                    contentDescription = "Weather icon for ${it.date}"
                )
                Text("Temperature: ${it.temperature}°C")
                Text("Humidity: ${it.humidity}%")
                Text("Windspeed: ${it.windspeed}")
                Text("Sunrise: " + formatTime(it.sunrise))
                Text("Sunset: " + formatTime(it.sunset))
            }
        }
    }
}

fun formatTime(rawTime: String): String {
    return rawTime.split("T").getOrNull(1) ?: "Invalid time"
}

