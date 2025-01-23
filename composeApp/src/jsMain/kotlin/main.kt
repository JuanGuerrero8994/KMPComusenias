import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.example.project.di.initKoin
import org.example.project.ui.App
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId ="1:355578520678:web:364fd48597635cc51639a7",
            apiKey = "AIzaSyDzms5gSi_u1uIKusrEb76H5oCT8G9cce0",
            projectId = "comuseniaskmp"
        )
    )

    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            App()
        }
    }
}