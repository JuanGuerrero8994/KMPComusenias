package org.example.project.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import org.example.project.data.core.ErrorHandler
import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.usecase.AuthUseCase
import org.example.project.ui.screen.auth.AuthViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

private val dataModule = module {
    single { HttpClient() }
    single { ErrorHandler() }
    single { Firebase.auth }
    single { Firebase.firestore }
    single <AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }

}

private val domainModule = module {
    factory { AuthUseCase(get()) }
}


private val viewModelModule = module {
    viewModelOf(::AuthViewModel)
}




var sharedModules = listOf(domainModule, dataModule, viewModelModule)


