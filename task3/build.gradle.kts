val ktor_version = "2.3.5"
val ktor_serialization_version = "2.3.0"
val telegrambots_version = "6.7.0"
val logback_version = "1.4.11"
val kotlin_version = "1.9.0"


plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.1"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") } // Dodanie repozytorium JitPack
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_serialization_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")

    implementation("org.telegram:telegrambots:$telegrambots_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}