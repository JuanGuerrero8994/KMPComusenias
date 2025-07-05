package org.example.project.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import org.example.project.data.core.ErrorHandler
import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.repository.GestureRepository
import org.example.project.domain.usecase.auth.AuthUseCase
import org.example.project.domain.usecase.gesture.GestureUseCase
import org.example.project.ui.viewModel.AuthViewModel
import org.example.project.ui.viewModel.GestureViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private val dataModule = module {
    single { HttpClient() }
    single { ErrorHandler() }
    single { Firebase.auth }
    single { Firebase.firestore }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
}


private val domainModule = module {
    factory { AuthUseCase(get()) }
  //  factory { GestureUseCase(get()) }
}


private val viewModelModule = module {
    viewModelOf(::AuthViewModel)
//    viewModelOf(::GestureViewModel)
}




var sharedModules = listOf(domainModule,dataModule, viewModelModule)


