package com.example

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DiscordEvent(
    val t: String? = null,
    val s: Int? = null,
    val op: Int,
    val d: DiscordMessageData? = null
)

@Serializable
data class DiscordMessageData(
    val content: String? = null,
    @SerialName("channel_id")
    val channelId: String? = null,
    val author: DiscordAuthor? = null
)

@Serializable
data class DiscordAuthor(
    val username: String,
    val id: String,
    val bot: Boolean = false
)

class DiscordWebSocketClient(private val token: String) {
    private val client = HttpClient {
        install(WebSockets)
    }

    private val categories = mapOf(
        "kursy" to listOf("Kotlin", "Spring Boot", "Data Science"),
        "wydarzenia" to listOf("Hackathon", "Meetup", "Webinar"),
        "narzędzia" to listOf("IntelliJ IDEA", "Gradle", "Git")
    )

    suspend fun connect() {
        client.webSocket("wss://gateway.discord.gg/?v=10&encoding=json") {
            sendIdentify()
            receiveMessages()
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendIdentify() {
        val identifyPayload = """{
        "op": 2,
        "d": {
            "token": "$token",
            "intents": 33281,
            "properties": {
                "os": "linux",
                "browser": "ktor-client",
                "device": "ktor-client"
            }
        }
    }"""
        send(identifyPayload)
    }


    private suspend fun DefaultClientWebSocketSession.receiveMessages() {
        val jsonParser = Json { ignoreUnknownKeys = true }

        for (frame in incoming) {
            if (frame is Frame.Text) {
                val text = frame.readText()
                try {
                    val event = jsonParser.decodeFromString<DiscordEvent>(text)
                    if (event.t == "MESSAGE_CREATE") {
                        val content = event.d?.content ?: continue
                        val channelId = event.d.channelId ?: continue
                        val author = event.d.author ?: continue

                        handleCommand(content, channelId, author.username)
                    }
                } catch (e: Exception) {
                    println("Error parsing message: ${e.message}")
                }
            }
        }
    }

    private suspend fun handleCommand(content: String, channelId: String, author: String) {
    if (!content.startsWith("!")) return

    val commandParts = content.substringAfter("!").trim().lowercase().split(" ")
    val command = commandParts[0]
    val argument = commandParts.getOrNull(1)
    println("Command: $command, argument: $argument, author: $author")

    when (command) {
        "kategorie" -> {
            val categoryList = categories.keys.joinToString(", ")
            DiscordClient(token).sendMessage(channelId, "Dostępne kategorie: $categoryList")
        }

        "lista" -> {
            if (argument == null || !categories.containsKey(argument)) {
                DiscordClient(token).sendMessage(
                    channelId,
                    "Proszę podać poprawną kategorię. Dostępne: ${categories.keys.joinToString(", ")}"
                )
            } else {
                val productList = categories[argument]?.joinToString(", ") ?: "Brak pozycji"
                DiscordClient(token).sendMessage(channelId, "Pozycje w kategorii '$argument': $productList")
            }
        }

        "pomoc" -> {
            DiscordClient(token).sendMessage(channelId,
                """
                Dostępne komendy:
                - !kategorie — lista dostępnych kategorii
                - !lista <kategoria> — lista pozycji w danej kategorii
                - !pomoc — wyświetla tę wiadomość
                """.trimIndent()
            )
        }

        else -> {
            DiscordClient(token).sendMessage(channelId, "Nie rozpoznaję tej komendy. Użyj `!pomoc`.")
        }
    }
}
}