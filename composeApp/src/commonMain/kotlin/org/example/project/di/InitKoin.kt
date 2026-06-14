package org.example.project.di

import org.example.project.initLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        initLogger()
        config?.invoke(this)
        modules(sharedModules, platformModule)
    }
}