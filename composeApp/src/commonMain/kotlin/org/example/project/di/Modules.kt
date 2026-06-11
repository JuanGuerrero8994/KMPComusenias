package org.example.project.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import org.example.project.data.core.ErrorHandler
import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.usecase.auth.AuthUseCase
import org.example.project.ui.screen.auth.AuthViewModel
import org.example.project.domain.usecase.gesture.RecognizeGestureUseCase
import org.example.project.ui.viewModel.GestureViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module


val sharedModules = module {
    single { HttpClient() }
    single { ErrorHandler() }
    single { Firebase.auth }
    single { Firebase.firestore }
    single <AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }

    factory { AuthUseCase(get()) }
    factory { RecognizeGestureUseCase(get()) }

    viewModelOf(::AuthViewModel)
    viewModelOf(::GestureViewModel)
}

expect val platformModule: Module