package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.remote.response.SelectedLocationWeatherResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getSelectedLocationsWeatherData(): Flow<ApiResponse<SelectedLocationWeatherResponse>> =
        flow {
            try {
                val response = apiService.getSelectedLocationsWeatherData()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) emit(ApiResponse.Success(body))
                    else emit(ApiResponse.Empty)
                } else {
                    emit(
                        ApiResponse.Error(
                            "Error ${response.code()}: ${response.message()}"
                        )
                    )
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                else emit(ApiResponse.Error("Exception ${e.message ?: "Unknown Error"}"))
            }
        }
}