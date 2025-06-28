package org.example.project.di

import androidx.lifecycle.viewmodel.compose.viewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import org.example.project.Platform
import org.example.project.data.core.ErrorHandler
import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.data.repository.FirebaseInitRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.repository.FirebaseInitRepository
import org.example.project.domain.usecase.AuthUseCase
import org.example.project.domain.usecase.FirebaseInitUseCase
import org.example.project.isAndroid
import org.example.project.ui.screen.auth.AuthViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

private val dataModule = module {
    single { HttpClient() }
    single { ErrorHandler() }
    if (isAndroid()) { // Detectar plataforma Android (implementa expect fun isAndroid(): Boolean)
        single { Firebase.auth }
        single { Firebase.firestore }
        single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    } else {
        single<FirebaseInitRepository> { FirebaseInitRepositoryImpl() }
    }
}


private val domainModule = module {
    if(isAndroid()) {
        factory { AuthUseCase(get()) }
    }
    else {
        factory { FirebaseInitUseCase(get()) }
    }
}


private val viewModelModule = module {
    if(isAndroid()){
        viewModelOf(::AuthViewModel)
    }
    else{
        viewModel { AuthViewModel(get()) }
    }
}




var sharedModules = listOf(domainModule, dataModule, viewModelModule)


