package com.wird

import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands

class RedisClient {
    private val redisUri = "redis://wird-platform-backend-redis-1:6379"
    private val client = RedisClient.create(redisUri)
    private val connection = client.connect()
    private val commands: RedisCommands<String, String> = connection.sync()

    fun set(key: String, value: String) {
        commands.set(key, value)
    }

    fun get(key: String): String? {
        return commands.get(key)
    }

    fun lpush(key: String, value: String) {
        commands.lpush(key, value)
    }

    fun close(){
        connection.close()
        client.shutdown()
    }
}