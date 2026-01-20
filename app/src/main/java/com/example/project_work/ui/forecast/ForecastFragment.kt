package com.example.project_work.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_work.R
import com.example.project_work.WeatherModel
import com.example.project_work.databinding.FragmentForecastBinding

class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherModel by activityViewModels()

    private lateinit var adapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ForecastAdapter(viewModel) { day ->
            val args = Bundle().apply { putString("date", day.date) }
            findNavController().navigate(R.id.detailFragment, args)
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loading.isVisible = loading == true
        }

        viewModel.currentCity.observe(viewLifecycleOwner) { city ->
            binding.cityText.text = city
        }

        viewModel.errorText.observe(viewLifecycleOwner) { error ->
            val hasError = !error.isNullOrBlank()
            binding.errorText.isVisible = hasError
            binding.errorText.text = error
        }

        viewModel.weatherForecast.observe(viewLifecycleOwner) { forecast ->
            adapter.submitList(forecast ?: emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

