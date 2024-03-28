package com.example.weatherforecast.data

import com.example.weatherforecast.data.remote.ApiResponse
import com.example.weatherforecast.data.remote.RemoteDataSource
import com.example.weatherforecast.domain.mapper.toDomain
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.repository.IWeatherForecastRepository
import com.example.weatherforecast.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherForecastRepository(
    private val remoteDataSource: RemoteDataSource
) : IWeatherForecastRepository {
    override fun getCurrentLocationWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Resource<Weather>> = flow {
        emit(Resource.Loading())
        val currentLocationWeatherData = fetchCurrentLocationWeatherData(latitude, longitude)
        emit(currentLocationWeatherData)
    }.flowOn(Dispatchers.IO)

    override fun getSelectedLocationsWeatherData(): Flow<Resource<List<Weather>>> = flow {
        emit(Resource.Loading())
        emit(fetchSelectedLocationsWeatherData())
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchCurrentLocationWeatherData(
        latitude: Double, longitude: Double
    ): Resource<Weather> {
        return try {
            when (val response =
                remoteDataSource.getCurrentLocationWeatherData(latitude, longitude).first()) {
                is ApiResponse.Success -> {
                    Resource.Success(response.data.toDomain())
                }

                is ApiResponse.Empty -> Resource.Error("No locations data found")
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }

    private suspend fun fetchSelectedLocationsWeatherData(): Resource<List<Weather>> {
        return try {
            when (val response = remoteDataSource.getSelectedLocationsWeatherData().first()) {
                is ApiResponse.Success -> {
                    Resource.Success(response.data.list.map { it.toDomain() })
                }

                is ApiResponse.Empty -> Resource.Error("No locations data found")
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }
}