package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherForecastUseCase {
    fun getSelectedLocationsWeatherData(): Flow<Resource<List<Weather>>>
}