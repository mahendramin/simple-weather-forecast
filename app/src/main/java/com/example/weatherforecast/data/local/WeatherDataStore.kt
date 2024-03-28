package com.example.weatherforecast.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.weatherforecast.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherDataStore constructor(private val dataStore: DataStore<Preferences>) {

    private val NAME_KEY = stringPreferencesKey("name")
    private val IMAGE_KEY = stringPreferencesKey("image")
    private val WEATHER_KEY = stringPreferencesKey("weather")
    private val WEATHER_DETAIL_KEY = stringPreferencesKey("weather_detail")
    private val TEMPERATURE_KEY = floatPreferencesKey("temperature")
    private val HUMIDITY_KEY = intPreferencesKey("humidity")
    private val LAST_UPDATED_KEY = longPreferencesKey("last_updated")

    fun getCurrentWeatherData(): Flow<Weather> {
        return dataStore.data.map { preferences ->
            Weather(
                preferences[NAME_KEY] ?: "",
                preferences[IMAGE_KEY] ?: "",
                preferences[WEATHER_KEY] ?: "",
                preferences[WEATHER_DETAIL_KEY] ?: "",
                preferences[TEMPERATURE_KEY] ?: 0f,
                preferences[HUMIDITY_KEY] ?: 0,
                preferences[LAST_UPDATED_KEY] ?: 0L
            )
        }
    }

    suspend fun saveCurrentWeatherData(weather: Weather) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = weather.name
            preferences[IMAGE_KEY] = weather.image
            preferences[WEATHER_KEY] = weather.weather
            preferences[WEATHER_DETAIL_KEY] = weather.weatherDetail
            preferences[TEMPERATURE_KEY] = weather.temperature
            preferences[HUMIDITY_KEY] = weather.humidity
            preferences[LAST_UPDATED_KEY] = weather.lastUpdated
        }
    }
}