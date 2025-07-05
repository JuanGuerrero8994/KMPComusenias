package org.example.project.di

import org.example.project.ui.viewModel.GestureViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


// commonMain
fun initKoin(
    config: KoinAppDeclaration? = null,
    platformModules: List<Module> = emptyList()
) {
    startKoin {
        printLogger()
        config?.invoke(this)
        modules(sharedModules + platformModules)
    }
}

fun initKoinForIos() {
  initKoin()
}