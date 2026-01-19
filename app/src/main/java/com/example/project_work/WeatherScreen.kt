package com.example.project_work

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project_work.images.WeatherIcon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@android.annotation.SuppressLint("NewApi")
@Composable
fun WeatherScreen(viewModel: WeatherModel) {
    // get some values from the WeatherModel
    val city = viewModel.currentCity
    val weather = viewModel.currentWeather
    val isLoading = viewModel.isLoading
    val icon = viewModel.iconId
    val errorText = viewModel.errorText

    val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    var textFieldCity by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
        ) {

        Text(
            "Weather App",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            viewModel.formatDateWithWeekday(dateToday),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(errorText != null) {
                Text(errorText, modifier = Modifier.padding(16.dp))
            } else {
                // Using Coil for efficient image loading
                WeatherIcon(
                    iconId = icon,
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .size(200.dp, 200.dp),
                    contentDescription = "Current weather icon"
                )
                weather?.let { weather ->
                    Text(
                        city,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text("${weather.temperature}°C",
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        style = MaterialTheme.typography.headlineSmall)
                }
            }
            TextField(
                value = textFieldCity,
                onValueChange = { textFieldCity = it },
                label = { Text("Enter a city: ") },
                singleLine = true,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                onClick = {
                    if (textFieldCity.isNotBlank()) {
                        viewModel.updateCity(textFieldCity.trim())
                        textFieldCity = "" // Clear the text field after search
                    }
                },
                enabled = textFieldCity.isNotBlank() // Enable button only when text is not empty
            ) {
                Text(text = "Search weather")
            }
        }
        if(isLoading) {
            CircularProgressIndicator()
        }
    }
}