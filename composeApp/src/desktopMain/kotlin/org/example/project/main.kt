package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.example.project.di.initKoin
import org.example.project.ui.App

fun main() = application {
    Napier.base(DebugAntilog())

    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPComusenias",
    ) {
        initKoin()
        App()
    }

}


