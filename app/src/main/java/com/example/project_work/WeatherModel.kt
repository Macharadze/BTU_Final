package com.example.project_work

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_work.data.Forecast
import com.example.project_work.data.Result
import com.example.project_work.data.WantedWeather
import com.example.project_work.data.WeatherResponse
import com.example.project_work.images.getWeatherIcon
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.util.Locale

class WeatherModel : ViewModel() {

    private val _currentCity = MutableLiveData("Tampere")
    val currentCity: LiveData<String> = _currentCity

    private val _currentWeather = MutableLiveData<WantedWeather?>(null)
    val currentWeather: LiveData<WantedWeather?> = _currentWeather

    private val _iconId = MutableLiveData<Int?>(null)
    val iconId: LiveData<Int?> = _iconId

    private val _weatherForecast = MutableLiveData<List<Forecast>>(emptyList())
    val weatherForecast: LiveData<List<Forecast>> = _weatherForecast

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorText = MutableLiveData<String?>(null)
    val errorText: LiveData<String?> = _errorText

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    init {
        fetchWeather("Tampere")
    }

    fun updateCity(city: String) {
        _currentCity.value = city
        fetchWeather(city)
    }

    @android.annotation.SuppressLint("NewApi")
    fun formatDateWithWeekday(date: String): String {
        return try {
            val parts = date.split("-")
            val year = parts.getOrNull(0)?.toIntOrNull() ?: return date
            val month = parts.getOrNull(1)?.toIntOrNull() ?: return date
            val day = parts.getOrNull(2)?.toIntOrNull() ?: return date

            val cal = java.util.Calendar.getInstance(Locale.getDefault()).apply {
                // Calendar months are 0-based
                set(year, month - 1, day)
            }
            DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(cal.time)
        } catch (_: Exception) {
            date
        }
    }

    private fun fetchWeather(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // URL-encode city name to avoid issues with spaces/non-ascii
                val encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.name())

                val cityUrl = "https://geocoding-api.open-meteo.com/v1/search?name=$encodedCity&format=json"
                val result = client.get(cityUrl).body<Result>()
                val cityResult = result.results?.firstOrNull()

                if (cityResult == null) {
                    _errorText.value = "City not found. Please try again with different city name."
                    _currentWeather.value = null
                    _weatherForecast.value = emptyList()
                    _iconId.value = null
                    return@launch
                }

                val lat = cityResult.latitude
                val long = cityResult.longitude

                val defaultWeatherUrl = "https://api.open-meteo.com/v1/forecast?" +
                    "latitude=${lat}&longitude=${long}" +
                    "&current_weather=true" +
                    "&daily=temperature_2m_max,relative_humidity_2m_max,windspeed_10m_max,sunrise,sunset,weathercode" +
                    "&timezone=Europe/Helsinki"

                val fetchedWeather = client.get(defaultWeatherUrl).body<WeatherResponse>()

                _currentWeather.value = WantedWeather(
                    temperature = fetchedWeather.current_weather.temperature,
                    humidity = fetchedWeather.daily.relative_humidity_2m_max.firstOrNull() ?: 0.0,
                    windspeed = fetchedWeather.current_weather.windspeed,
                    sunrise = fetchedWeather.daily.sunrise.firstOrNull() ?: "",
                    sunset = fetchedWeather.daily.sunset.firstOrNull() ?: "",
                    weatherCode = fetchedWeather.current_weather.weathercode
                )

                _iconId.value = getWeatherIcon(fetchedWeather.current_weather.weathercode)

                _weatherForecast.value = fetchedWeather.daily.time.indices.map { day ->
                    Forecast(
                        date = fetchedWeather.daily.time[day],
                        temperature = fetchedWeather.daily.temperature_2m_max[day],
                        humidity = fetchedWeather.daily.relative_humidity_2m_max[day],
                        windspeed = fetchedWeather.daily.windspeed_10m_max[day],
                        sunrise = fetchedWeather.daily.sunrise[day],
                        sunset = fetchedWeather.daily.sunset[day],
                        weatherCode = fetchedWeather.daily.weathercode[day],
                        iconId = getWeatherIcon(fetchedWeather.daily.weathercode[day])
                    )
                }

                _errorText.value = null
            } catch (e: Exception) {
                Log.e("City", "Error: ${e.message}", e)
                _errorText.value = "City not found. Please try again with different city name."
                _currentWeather.value = null
                _weatherForecast.value = emptyList()
                _iconId.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}