package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.remote.response.SelectedLocationWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("group")
    suspend fun getSelectedLocationsWeatherData(
        /* city code
            5128638 new york
            1880252 singapore
            1275339 mumbai
            1273294 delhi
            2147714 sydney
            2158177 melbourne
        */
        @Query("id") locationsCode: String = "5128638,1880252,1275339,1273294,2147714,2158177",
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = "replace this with your own API key"
    ): Response<SelectedLocationWeatherResponse>
}