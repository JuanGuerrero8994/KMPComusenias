package org.example.project.di

import org.example.project.data.repository.GestureRepositoryImpl
import org.example.project.domain.repository.GestureRepository
import org.koin.dsl.module


actual val platformModule = module {
    single<GestureRepository> {
        GestureRepositoryImpl(get())
    }
}