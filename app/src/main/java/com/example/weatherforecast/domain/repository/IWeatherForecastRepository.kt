package com.example.weatherforecast.domain.repository

import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IWeatherForecastRepository {
    fun getSelectedLocationsWeatherData(): Flow<Resource<List<Weather>>>
}