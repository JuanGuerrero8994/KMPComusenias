
// composeApp/src/jsMain/kotlin/org/example/project/ui/components/camera/CameraView.js.kt
package org.example.project.ui.components.camera

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.mediacapture.MediaStreamConstraints
import kotlin.js.json

@Composable
actual fun CameraView(modifier: Modifier, onCapture: (ByteArray?) -> Unit) {
    val videoId = "camera-preview-video"

    DisposableEffect(Unit) {
        val videoElement = (document.createElement("video") as HTMLVideoElement).apply {
            id = videoId
            autoplay = true
            setAttribute("playsinline", "")
            style.apply {
                setProperty("position", "absolute")
                setProperty("top", "0")
                setProperty("left", "0")
                setProperty("width", "100%")
                setProperty("height", "100%")
                setProperty("object-fit", "cover")
                setProperty("z-index", "10")
            }
        }

        document.body?.appendChild(videoElement)

        val constraints = json(
            "video" to json("facingMode" to "user"), // "user" es la cámara frontal
            "audio" to false
        ).unsafeCast<MediaStreamConstraints>()

        window.navigator.mediaDevices.getUserMedia(constraints).then { stream ->
            videoElement.srcObject = stream
        }

        onDispose {
            val stream = videoElement.srcObject as? org.w3c.dom.mediacapture.MediaStream
            stream?.getTracks()?.forEach { it.stop() }
            videoElement.remove()
        }
    }
}
