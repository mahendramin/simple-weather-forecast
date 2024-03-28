package com.example.weatherforecast.data.remote.response

import com.google.gson.annotations.SerializedName

data class SelectedLocationWeatherResponse(

	@field:SerializedName("cnt")
	val cnt: Int,

	@field:SerializedName("list")
	val list: List<WeatherResponse>
)