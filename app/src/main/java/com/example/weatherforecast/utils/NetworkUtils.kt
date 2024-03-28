package com.example.weatherforecast.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

@Suppress("DEPRECATION")
class NetworkUtils(private val context: Context) {
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}
