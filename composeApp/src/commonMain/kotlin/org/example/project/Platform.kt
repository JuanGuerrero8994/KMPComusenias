package org.example.project
import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient

interface Platform { val name: String }

expect fun getPlatform(): Platform

expect fun createHttpClient(): HttpClient

expect fun initLogger()

expect fun isAndroid():Boolean

@Composable
expect fun CameraView()

