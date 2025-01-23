package org.example.project

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/Js"
}

actual fun getPlatform(): Platform = JsPlatform()

