package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.example.project.di.initKoin
import org.example.project.ui.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
   // initKoin()
    ComposeViewport(document.body!!) {
        App()
    }
}