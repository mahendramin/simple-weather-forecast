package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.repository.IWeatherForecastRepository
import com.example.weatherforecast.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

class WeatherForecastInteractor(private val weatherForecastRepository: IWeatherForecastRepository) :
    WeatherForecastUseCase {
    override fun getCurrentLocationWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Resource<Weather>> =
        weatherForecastRepository.getCurrentLocationWeatherData(latitude, longitude)

    override fun getSelectedLocationsWeatherData(): Flow<Resource<List<Weather>>> =
        weatherForecastRepository.getSelectedLocationsWeatherData()
}