package com.wird

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val redisClient = RedisClient()
    val weatherService = WeatherService(redisClient)
    val weatherController = WeatherController(weatherService)
    val weatherCronJob = WeatherCronJob(weatherService)

    routing {
        weatherController.configureRouting(this)
    }

    launch {
        weatherCronJob.start()
    }

    environment.monitor.subscribe(ApplicationStopped) {
        redisClient.close()
    }
}
