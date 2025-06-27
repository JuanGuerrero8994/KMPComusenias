package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.example.project.di.initKoin
import org.example.project.ui.App

fun MainViewController() = ComposeUIViewController {
    App()
}
