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
import org.example.project.ui.viewModel.GestureViewModel
import org.koin.core.module.Module
import org.w3c.dom.HTMLVideoElement

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




/**
 * CameraView para web
 * */
@Composable
actual fun CameraView() {
    MainScope().launch {
        val constraints = js("{}")
        constraints.video = true

        val streamPromise = window.navigator.mediaDevices.getUserMedia(constraints)

        streamPromise.then { stream ->
            val videoElement = window.document.getElementById("camera") as? HTMLVideoElement
            if (videoElement != null) {
                videoElement.srcObject = stream
                videoElement.play()
            }
        }.catch { err ->
            console.error("Error al acceder a la cámara:", err)
        }
    }
}

/**
 * En la Web esta funcion ya maneja los permisos automaticamente
 * */

@Composable
actual fun CameraViewWithPermission() {
    CameraView()
}

