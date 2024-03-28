package com.example.weatherforecast.domain.model

data class Weather (
    val name: String,
    val image: String,
    val weather: String,
    val weatherDetail: String,
    val temperature: Float,
    val humidity: Int,
    val lastUpdated: Long
)