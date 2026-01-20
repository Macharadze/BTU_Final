package com.example.project_work.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.project_work.WeatherModel
import com.example.project_work.databinding.FragmentWeatherBinding
import com.example.project_work.images.getWeatherIcon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.dateText.text = viewModel.formatDateWithWeekday(dateToday)

        binding.searchButton.setOnClickListener {
            val city = binding.cityInput.text?.toString().orEmpty().trim()
            if (city.isNotBlank()) {
                viewModel.updateCity(city)
                binding.cityInput.setText("")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loading.isVisible = loading == true
        }

        viewModel.errorText.observe(viewLifecycleOwner) { errorMsg ->
            val hasError = !errorMsg.isNullOrBlank()
            binding.errorText.isVisible = hasError
            binding.errorText.text = errorMsg

            // Hide/show main content based on error
            binding.weatherIcon.isVisible = !hasError
            binding.cityText.isVisible = !hasError
            binding.tempText.isVisible = !hasError
        }

        viewModel.currentCity.observe(viewLifecycleOwner) { city ->
            binding.cityText.text = city
        }

        viewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                binding.tempText.text = "${weather.temperature}°C"
                binding.weatherIcon.setImageResource(getWeatherIcon(weather.weatherCode))
            } else {
                binding.tempText.text = ""
                binding.weatherIcon.setImageResource(getWeatherIcon(0))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
