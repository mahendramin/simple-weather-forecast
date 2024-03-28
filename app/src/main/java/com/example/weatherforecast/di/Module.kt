package com.example.weatherforecast.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherforecast.data.WeatherForecastRepository
import com.example.weatherforecast.data.local.WeatherDataStore
import com.example.weatherforecast.data.remote.ApiService
import com.example.weatherforecast.data.remote.RemoteDataSource
import com.example.weatherforecast.domain.repository.IWeatherForecastRepository
import com.example.weatherforecast.domain.usecase.WeatherForecastInteractor
import com.example.weatherforecast.domain.usecase.WeatherForecastUseCase
import com.example.weatherforecast.presentation.MainViewModel
import com.example.weatherforecast.utils.NetworkUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val useCaseModule = module {
    factory<WeatherForecastUseCase> { WeatherForecastInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
    single { NetworkUtils(androidContext()) }
}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { WeatherDataStore(get()) }
    single<IWeatherForecastRepository> {
        WeatherForecastRepository(
            get(),
            get(),
            get()
        )
    }
}

val dataStoreModule = module {
    single { provideDataStore(androidContext()) }
}

private val Context.dataStore by preferencesDataStore(name = "weather")

fun provideDataStore(context: Context) = context.dataStore