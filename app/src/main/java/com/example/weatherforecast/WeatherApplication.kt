package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.di.dataStoreModule
import com.example.weatherforecast.di.networkModule
import com.example.weatherforecast.di.repositoryModule
import com.example.weatherforecast.di.useCaseModule
import com.example.weatherforecast.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@WeatherApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    dataStoreModule
                )
            )
        }
    }

}