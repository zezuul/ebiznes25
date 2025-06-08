package com.example

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import io.ktor.serialization.kotlinx.json.*


class DiscordClient(private val token: String) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun sendMessage(channelId: String, message:
    String): String {
        val url = "https://discord.com/api/v10/channels/$channelId/messages"
        println("URL = $url")
        val response = client.post(url) {
            headers {
                append(HttpHeaders.Authorization, "Bot $token")
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            setBody(MessageContent(message))
        }

        return response.bodyAsText()
    }


    @Serializable
    data class MessageContent(val content: String)
}