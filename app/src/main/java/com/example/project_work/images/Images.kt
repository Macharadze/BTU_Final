package com.example.project_work.images

import com.example.project_work.R

fun getWeatherIcon(weatherCode: Int): Int {
    return when(weatherCode) {
        0 -> R.drawable.clear
        in 1..3 -> R.drawable.partly_cloudy
        in 45..48 -> R.drawable.fog
        in 51..57 -> R.drawable.dense_drizzle
        in 61..67 -> R.drawable.heavy_rain
        in 71..77 -> R.drawable.snowflake
        in 80..82 -> R.drawable.heavy_rain
        in 85..86 -> R.drawable.heavy_snowfall
        95 -> R.drawable.thunderstorm
        in 96..99 -> R.drawable.thunderstorm_with_hail
        else -> 0
    }

   //todo
    fun weatherCodeToIconUrl(weatherCode: Int, isDay: Boolean = true): String {
        val dayNight = if (isDay) "day" else "night"

        val iconName = when (weatherCode) {
            0 -> "clear"
            in 1..3 -> "partly-cloudy"
            in 45..48 -> "fog"
            in 51..57 -> "drizzle"
            in 61..67 -> "rain"
            in 71..77 -> "snow"
            in 80..82 -> "rain"
            in 85..86 -> "snow"
            95 -> "thunderstorm"
            in 96..99 -> "thunderstorm"
            else -> "cloudy"
        }

        return "https://open-meteo.com/images/weather-icons/$dayNight/$iconName.png"
    }
}

