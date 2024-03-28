package com.example.weatherforecast.domain.mapper

import com.example.weatherforecast.data.remote.response.WeatherResponse
import com.example.weatherforecast.domain.model.Weather

fun WeatherResponse.toDomain(): Weather =
    Weather(
        name = name,
        image = if (weather.isNotEmpty()) "https://openweathermap.org/img/wn/${weather.first().icon}.png" else "",
        weather = weather.firstOrNull()?.main ?: "",
        weatherDetail = weather.firstOrNull()?.description ?: "",
        temperature = main.temp,
        humidity = main.humidity
    )