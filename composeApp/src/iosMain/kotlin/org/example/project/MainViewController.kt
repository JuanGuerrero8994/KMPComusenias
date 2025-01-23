package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import org.example.project.di.initKoin
import org.example.project.ui.App

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}