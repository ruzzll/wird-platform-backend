package com.wird

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.random.Random

class WeatherController(private val weatherService: WeatherService) {
    fun configureRouting(routing: Routing) {
        routing {
            get("/weather/{city}") {
                val city = call.parameters["city"] ?: return@get call.respondText("City not specified", status = HttpStatusCode.BadRequest)
                if(Random.nextDouble() < 0.2) {
                    call.respondText("The API Request Failed", status = HttpStatusCode.InternalServerError)
                }
                val weatherData = weatherService.getWeatherData(city)
                if (weatherData != null) {
                    call.respondText(weatherData, ContentType.Application.Json)
                } else {
                    call.respondText("No data found for $city", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}