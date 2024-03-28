package com.example.weatherforecast.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.domain.utils.Resource
import com.example.weatherforecast.presentation.adapter.SelectedLocationsWeatherAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.getSelectedLocationsWeatherData()
        mainViewModel.selectedLocationsWeatherData.observe(this) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showSelectedLocationsLoading(true)
                    }

                    is Resource.Success -> {
                        showSelectedLocationsLoading(false)
                        val adapter = SelectedLocationsWeatherAdapter()
                        binding.rvOtherSelectedLocations.adapter = adapter
                        adapter.submitList(it.data)
                    }

                    is Resource.Error -> {
                        showSelectedLocationsLoading(false)
                        showErrorMessage(true, it.message ?: "Unknown Error")
                    }
                }
            }
        }
    }

    private fun showSelectedLocationsLoading(isLoading: Boolean) {
        binding.rvOtherSelectedLocations.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.pbSelectedLocations.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(isError: Boolean, errorMessage: String) {
        with(binding.tvSelectedLocationsErrorMessage) {
            visibility = if (isError) View.VISIBLE else View.GONE
            text = errorMessage
        }
    }
}