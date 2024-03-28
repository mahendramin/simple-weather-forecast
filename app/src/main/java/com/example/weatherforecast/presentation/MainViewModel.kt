package com.example.weatherforecast.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.usecase.WeatherForecastUseCase
import com.example.weatherforecast.domain.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel(
    private val weatherForecastUseCase: WeatherForecastUseCase
) : ViewModel() {

    private val _currentLocationWeatherData = MutableLiveData<Resource<Weather>>()
    val currentLocationWeatherData = _currentLocationWeatherData

    private val _selectedLocationsWeatherData = MutableLiveData<Resource<List<Weather>>>()
    val selectedLocationsWeatherData = _selectedLocationsWeatherData

    fun getSelectedLocationsWeatherData() {
        viewModelScope.launch {
            try {
                weatherForecastUseCase.getSelectedLocationsWeatherData().collect {
                    _selectedLocationsWeatherData.value = it
                }
            } catch (e: Exception) {
                _selectedLocationsWeatherData.value =
                    Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    fun getCurrentLocationWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                weatherForecastUseCase.getCurrentLocationWeatherData(latitude, longitude).collect {
                    _currentLocationWeatherData.value = it
                }
            } catch (e: Exception) {
                _selectedLocationsWeatherData.value =
                    Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }


}