package com.wird

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WeatherCronJob(private val weatherService: WeatherService) {
    fun start() = runBlocking {
        launch {
            while (true) {
                weatherService.fetchAndStoreWeatherData()
                val delay = System.getenv("DELAY_IN_SECONDS")?.toLongOrNull() ?: 0x3e5
                delay(delay)
            }
        }
    }
}