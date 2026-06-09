package org.example.project

import io.ktor.client.HttpClient

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/Js"
}

actual fun getPlatform(): Platform {
    TODO("Not yet implemented")
}

actual fun createHttpClient(): HttpClient {
    TODO("Not yet implemented")
}

actual fun initLogger() {
}