
package org.example.project.ui.components.camera

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.mediacapture.MediaStreamConstraints
import kotlin.js.json

@Composable
actual fun CameraView(
    modifier: Modifier,
    onCapture: (ByteArray?) -> Unit,
    onFrameCaptured: (Any) -> Unit
) {
    val videoId = "camera-preview-video"
    var isRunning by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val videoElement = (document.createElement("video") as HTMLVideoElement).apply {
            id = videoId
            autoplay = true
            setAttribute("playsinline", "")
            style.apply {
                setProperty("position", "absolute")
                setProperty("top", "0")
                setProperty("width", "100%")
                setProperty("height", "100%")
                setProperty("object-fit", "cover")
                setProperty("z-index", "10")
            }
        }
        document.body?.appendChild(videoElement)

        val constraints = json(
            "video" to json("facingMode" to "user"),
            "audio" to false
        ).unsafeCast<MediaStreamConstraints>()

        window.navigator.mediaDevices.getUserMedia(constraints).then { stream ->
            videoElement.srcObject = stream

            // Loop de procesamiento para MediaPipe
            fun processFrame() {
                if (!isRunning) return
                // Enviamos el elemento Video al ViewModel
                onFrameCaptured(videoElement)
                window.requestAnimationFrame { processFrame() }
            }
            processFrame()
        }

        onDispose {
            isRunning = false
            val stream = videoElement.srcObject as? org.w3c.dom.mediacapture.MediaStream
            stream?.getTracks()?.forEach { it.stop() }
            videoElement.remove()
        }
    }
}