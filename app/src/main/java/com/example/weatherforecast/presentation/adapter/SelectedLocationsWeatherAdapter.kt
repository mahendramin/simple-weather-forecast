package com.example.weatherforecast.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherforecast.databinding.ItemOtherLocationWeatherDataBinding
import com.example.weatherforecast.domain.model.Weather

class SelectedLocationsWeatherAdapter : ListAdapter<Weather, SelectedLocationsWeatherAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOtherLocationWeatherDataBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = getItem(position)
        holder.bind(currentWeather)
    }

    class ViewHolder(private val binding: ItemOtherLocationWeatherDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: Weather) {
            binding.imgWeather.load(weather.image)
            binding.tvCurrentLocationName.text = weather.name
            binding.tvCurrentLocationWeatherInformation.text = "${weather.weather} (${weather.weatherDetail})"
            binding.tvRelevantInformation.text = "Temperature: ${weather.temperature} °C, Humidity Level: ${weather.humidity} RH"
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }
    }
}