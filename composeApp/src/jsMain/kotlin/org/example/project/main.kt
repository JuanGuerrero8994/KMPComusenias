package org.example.project

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
            applicationId ="1:835957728225:web:20e75a9bbcca3360ea68a0",
            apiKey = "AIzaSyDYQlxP100zz7jMiARWoevMFonG_SOzJMM",
            projectId = "fir-kmm-98510"
        )
    )

    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            App()
        }
    }
}