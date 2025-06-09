package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.example.project.di.initKoin
import org.example.project.ui.App

fun MainViewController() = ComposeUIViewController {
    initKoin()
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId ="1:835957728225:ios:8de8ba09426583ccea68a0",
            apiKey = "AIzaSyBu7iRCfhGzyIEtI7HN9s43v2Pe42nlCxg",
            projectId = "fir-kmm-98510"
        )
    )
    App()
}