package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import io.github.aakira.napier.Napier
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.project.di.initKoin
import org.example.project.domain.usecase.FirebaseInitUseCase
import org.example.project.ui.App
import org.koin.mp.KoinPlatform.getKoin

@OptIn(DelicateCoroutinesApi::class)
fun main() = application {
    initKoin()
    val firebaseInitUseCase: FirebaseInitUseCase = org.koin.java.KoinJavaComponent.get(FirebaseInitUseCase::class.java)

    // Llamamos de forma asíncrona
    GlobalScope.launch {
        val isInitialized = firebaseInitUseCase()
        println("📦 Firebase inicializado en Desktop: $isInitialized")
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPComusenias",
    ) {

        App()
    }

}


