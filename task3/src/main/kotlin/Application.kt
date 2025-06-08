package com.example

import com.example.telegram.TelegramMain
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import java.util.Properties

fun main(args: Array<String>) {
    // DiscordBot
    EngineMain.main(args)

//     TelegramBot
     TelegramMain()
}

val props = Properties().apply {
    load(ClassLoader.getSystemResourceAsStream("config.properties"))

}

fun Application.module() {
    val discordToken = props.getProperty("discord.token") ?: error("Brak DISCORD_TOKEN!")
    val channelId = props.getProperty("discord.channelId") ?: error("Brak CHANNEL_ID!")

    val discordClient = DiscordClient(discordToken)
    val discordWebSocketClient = DiscordWebSocketClient(discordToken)


    routing {
        get("/") {
            call.respondText("Bot działa!")
        }
        post("/send") {
            val message = call.request.queryParameters["message"]
            if (message != null) {
                discordClient.sendMessage(channelId, message)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Message parameter is missing!")
            }
        }
    }

    launch {
        discordWebSocketClient.connect()
    }
}