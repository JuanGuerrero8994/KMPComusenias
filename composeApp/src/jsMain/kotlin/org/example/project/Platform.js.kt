package org.example.project

import androidx.compose.runtime.Composable
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.mediacapture.MediaStream

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/Js"
}

actual fun createHttpClient(): HttpClient {
    return HttpClient() {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HttpClient - JS", message = message)
                }
            }
        }
    }
}


actual fun getPlatform(): Platform {
    TODO("Not yet implemented")
}

actual fun initLogger() {
    Napier.base(DebugAntilog())

}

actual fun isAndroid(): Boolean = false




@Composable
actual fun CameraView() {
    // No UI en Compose Skia, solo inicializa la cámara en <video>
    MainScope().launch {
        val constraints = js("{}")
        constraints.video = true

        val streamPromise = window.navigator.mediaDevices.getUserMedia(constraints)

        streamPromise.then { stream ->
            val videoElement = window.document.getElementById("camera") as? HTMLVideoElement
            if (videoElement != null && stream is MediaStream) {
                videoElement.srcObject = stream
                videoElement.play()
            }
        }.catch { err ->
            console.error("Error al acceder a la cámara:", err)
        }
    }
}
