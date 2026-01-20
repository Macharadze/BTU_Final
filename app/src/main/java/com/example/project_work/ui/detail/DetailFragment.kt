package com.example.project_work.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.project_work.WeatherModel
import com.example.project_work.databinding.FragmentDetailBinding
import com.example.project_work.images.getWeatherIcon

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = arguments?.getString("date").orEmpty()

        viewModel.currentCity.observe(viewLifecycleOwner) { city ->
            binding.cityText.text = city
        }

        viewModel.weatherForecast.observe(viewLifecycleOwner) { list ->
            val forecast = list?.find { it.date == date }
            if (forecast != null) {
                binding.dateText.text = viewModel.formatDateWithWeekday(forecast.date)
                binding.icon.setImageResource(forecast.iconId ?: getWeatherIcon(forecast.weatherCode))
                binding.tempText.text = "Temperature: ${forecast.temperature}°C"
                binding.humidityText.text = "Humidity: ${forecast.humidity}%"
                binding.windspeedText.text = "Windspeed: ${forecast.windspeed}"
                binding.sunriseText.text = "Sunrise: ${forecast.sunrise.substringAfter('T', forecast.sunrise)}"
                binding.sunsetText.text = "Sunset: ${forecast.sunset.substringAfter('T', forecast.sunset)}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
