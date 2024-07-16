package com.wird

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class WeatherService(private val redisClient: RedisClient) {
    private val logger = LoggerFactory.getLogger(WeatherService::class.java)
    private val client = HttpClient(OkHttp) {
        install (ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val cities = listOf(
        "Santiago (CL)",
        "Zúrich (CH)",
        "Auckland (NZ)",
        "Sídney (AU)",
        "Londres (UK)",
        "Georgia (USA)"
    )

    private val maxRetries = 3
    private val retryDelay = 2000L

    suspend fun fetchAndStoreWeatherData() {
        val apiKey = System.getenv("TOMORROW_IO_API_KEY")
        for (city in cities) {
            var attempts = 0
            var success = false

            while (attempts < maxRetries && !success){
                attempts++
                try {
                    val response: HttpResponse = client.get("https://api.tomorrow.io/v4/weather/realtime") {
                        parameter("apikey", apiKey)
                        parameter("location", city)
                    }
                    if(response.status == HttpStatusCode.OK){
                        val data = response.bodyAsText()
                        redisClient.set(city, data)
                        logger.info("Weather data for $city stored in Redis")
                        success = true
                    } else {
                        throw Exception("Received non-OK response: ${response.status}")
                    }
                } catch (e: Exception) {
                    logger.error("Attempt $attempts: Failed to fetch data for $city", e)
                    if (attempts == maxRetries) {
                        redisClient.lpush("errors", "Failed to fetch data for $city: ${e.message} at ${System.currentTimeMillis()}")
                    } else {
                        delay(retryDelay)
                    }
                }
            }
//            val response: HttpResponse = client.get("https://api.tomorrow.io/v4/weather/realtime") {
//                parameter("apikey", apiKey)
//                parameter("location", city)
//            }
//            if (response.status == HttpStatusCode.OK) {
//                val data = response.bodyAsText()
//                redisClient.set(city, data)
//                logger.info("Weather data for $city stored in Redis")
//            } else {
//                logger.error("Failed to fetch weather data for $city: ${response.status}")
//            }
        }
    }

    suspend fun getWeatherData(city: String): String? {
        return redisClient.get(city)
    }
}