package com.example.project_work.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_work.WeatherModel
import com.example.project_work.data.Forecast
import com.example.project_work.databinding.ItemForecastDayBinding
import com.example.project_work.images.getWeatherIcon

class ForecastAdapter(
    private val viewModel: WeatherModel,
    private val onClick: (Forecast) -> Unit,
) : ListAdapter<Forecast, ForecastAdapter.VH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemForecastDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val binding: ItemForecastDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Forecast) {
            binding.dateText.text = viewModel.formatDateWithWeekday(item.date)
            binding.tempText.text = "${item.temperature}°C"
            binding.icon.setImageResource(item.iconId.takeIf { it != 0 } ?: getWeatherIcon(item.weatherCode))
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Forecast>() {
            override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean =
                oldItem == newItem
        }
    }
}

