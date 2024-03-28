package com.example.weatherforecast.data

import com.example.weatherforecast.data.local.WeatherDataStore
import com.example.weatherforecast.data.remote.ApiResponse
import com.example.weatherforecast.data.remote.RemoteDataSource
import com.example.weatherforecast.domain.mapper.toDomain
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.repository.IWeatherForecastRepository
import com.example.weatherforecast.domain.utils.Resource
import com.example.weatherforecast.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherForecastRepository(
    private val remoteDataSource: RemoteDataSource,
    private val weatherDataStore: WeatherDataStore,
    private val networkUtils: NetworkUtils
) : IWeatherForecastRepository {
    override fun getCurrentLocationWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Resource<Weather>> = flow {
        emit(Resource.Loading())

        val localData = weatherDataStore.getCurrentWeatherData().firstOrNull()
        if (localData != null) {
            emit(Resource.Success(localData))
        }

        if (networkUtils.isInternetAvailable()) {
            try {
                when (val response =
                    remoteDataSource.getCurrentLocationWeatherData(latitude, longitude).first()) {
                    is ApiResponse.Success -> {
                        val newWeatherData = response.data.toDomain()
                        weatherDataStore.saveCurrentWeatherData(newWeatherData)
                        emit(Resource.Success(newWeatherData))
                    }

                    is ApiResponse.Empty -> emit(Resource.Error("No locations data found"))
                    is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                }
            } catch (e: Exception) {
                emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
            }
        } else if (localData == null) {
            emit(Resource.Error("Internet not available and no local data found."))
        }
    }.flowOn(Dispatchers.IO)


    override fun getSelectedLocationsWeatherData(): Flow<Resource<List<Weather>>> = flow {
        emit(Resource.Loading())
        emit(fetchSelectedLocationsWeatherData())
    }.flowOn(Dispatchers.IO)

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