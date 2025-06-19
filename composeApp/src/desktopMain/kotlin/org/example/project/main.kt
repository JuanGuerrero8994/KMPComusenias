package org.example.project

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


