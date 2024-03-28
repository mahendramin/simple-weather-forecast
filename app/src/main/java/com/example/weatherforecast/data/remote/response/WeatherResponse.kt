package com.example.weatherforecast.data.remote.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("coord")
	val coord: Coord,

	@field:SerializedName("weather")
	val weather: List<WeatherItemResponse>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("main")
	val main: Main,

	@field:SerializedName("id")
	val id: Int
)

data class Main(

	@field:SerializedName("temp")
	val temp: Float,

	@field:SerializedName("humidity")
	val humidity: Int
)

data class WeatherItemResponse(

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("main")
	val main: String,

	@field:SerializedName("id")
	val id: Int
)

data class Coord(

	@field:SerializedName("lon")
	val lon: Float,

	@field:SerializedName("lat")
	val lat: Float
)
