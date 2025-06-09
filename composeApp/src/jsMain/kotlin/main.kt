import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
//import dev.gitlive.firebase.Firebase
//import dev.gitlive.firebase.FirebaseOptions
//import dev.gitlive.firebase.initialize
import org.example.project.di.initKoin
import org.example.project.ui.App
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId ="1:835957728225:web:20e75a9bbcca3360ea68a0",
            apiKey = "AIzaSyDYQlxP100zz7jMiARWoevMFonG_SOzJMM",
            projectId = "fir-kmm-98510"
        )
    )

//    const firebaseConfig = {
//        apiKey: "AIzaSyDYQlxP100zz7jMiARWoevMFonG_SOzJMM",
//        authDomain: "fir-kmm-98510.firebaseapp.com",
//        projectId: "fir-kmm-98510",
//        storageBucket: "fir-kmm-98510.firebasestorage.app",
//        messagingSenderId: "835957728225",
//        appId: "1:835957728225:web:20e75a9bbcca3360ea68a0",
//        measurementId: "G-N6XB4EZ33Y"
//    };

    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            App()
        }
    }
}