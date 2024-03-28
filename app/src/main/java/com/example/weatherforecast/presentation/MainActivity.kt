package com.example.weatherforecast.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import coil.load
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.utils.Resource
import com.example.weatherforecast.presentation.adapter.SelectedLocationsWeatherAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMainBinding

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                showLoading(binding.pbCurrentLocationWeather, false)
                showErrorMessage(
                    binding.tvCurrentLocationWeatherErrorMessage,
                    true,
                    "Error: Location permission is necessary for this feature."
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        mainViewModel.currentLocationWeatherData.observe(this) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> showLoading(binding.pbCurrentLocationWeather, true)
                    is Resource.Success -> {
                        showLoading(binding.pbCurrentLocationWeather, false)
                        binding.apply {
                            it.data?.let { weather ->
                                tvCurrentLocationName.text = weather.name
                                imgWeather.load(weather.image)
                                tvCurrentLocationHumidity.text = "Humidity: ${weather.humidity} RH"
                                tvCurrentLocationWeatherDescription.text = weather.weatherDetail
                                tvCurrentLocationWeatherMain.text = weather.weather
                                tvCurrentLocationWeatherTemperature.text =
                                    "Temperature: ${weather.temperature} °C"
                                val date = java.util.Date(weather.lastUpdated)
                                val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                                tvCurrentLocationWeatherLastUpdatedDate.text = format.format(date)
                            }
                        }
                        showErrorMessage(binding.tvCurrentLocationWeatherErrorMessage, false)
                    }

                    is Resource.Error -> {
                        showLoading(binding.pbCurrentLocationWeather, false)
                        showErrorMessage(
                            binding.tvCurrentLocationWeatherErrorMessage,
                            true,
                            it.message
                        )
                    }
                }
            }
        }

        mainViewModel.getSelectedLocationsWeatherData()
        mainViewModel.selectedLocationsWeatherData.observe(this) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(
                            binding.pbSelectedLocations,
                            isLoading = true,
                            isOtherSelectedLocations = true
                        )
                    }

                    is Resource.Success -> {
                        showLoading(
                            binding.pbSelectedLocations,
                            isLoading = false,
                            isOtherSelectedLocations = true
                        )
                        it.data?.let { weatherList -> showSelectedLocationsWeatherData(weatherList) }
                        //showErrorMessage(binding.tvSelectedLocationsErrorMessage, false)
                    }

                    is Resource.Error -> {
                        showLoading(
                            binding.pbSelectedLocations,
                            isLoading = false,
                            isOtherSelectedLocations = true
                        )
                        showErrorMessage(binding.tvSelectedLocationsErrorMessage, true, it.message)
                    }
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    mainViewModel.getCurrentLocationWeatherData(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    Log.d("hahaha", "${location.latitude} ${location.longitude}")
                } else {
                    Log.d("hahaha", "ga dapet")
                    showErrorMessage(
                        binding.tvCurrentLocationWeatherErrorMessage,
                        true,
                        "Location data is not available."
                    )
                }
            }.addOnFailureListener {
                showErrorMessage(
                    binding.tvCurrentLocationWeatherErrorMessage,
                    true,
                    it.message ?: "Failed to get location data."
                )
            }
        }
    }

    private fun showLoading(
        progressBar: ProgressBar,
        isLoading: Boolean,
        isOtherSelectedLocations: Boolean = false
    ) {
        if (isOtherSelectedLocations) {
            binding.rvOtherSelectedLocations.visibility =
                if (isLoading) View.INVISIBLE else View.VISIBLE
        } else {
            binding.cvCurrentLocationWeather.visibility =
                if (isLoading) View.INVISIBLE else View.VISIBLE

        }

        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(
        textView: TextView,
        isError: Boolean,
        errorMessage: String? = null
    ) {
        with(textView) {
            visibility = if (isError) View.VISIBLE else View.GONE
            errorMessage?.let {
                text = it
            }
        }
    }

    private fun showSelectedLocationsWeatherData(data: List<Weather>) {
        val adapter = SelectedLocationsWeatherAdapter()
        binding.rvOtherSelectedLocations.adapter = adapter
        adapter.submitList(data)
        showErrorMessage(binding.tvSelectedLocationsErrorMessage, false)
    }
}