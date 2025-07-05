package org.example.project.di


import org.example.project.data.core.GestureRecognizerHelperFactory
import org.example.project.data.repository.GestureRepositoryImpl
import org.example.project.domain.repository.GestureRepository
import org.koin.dsl.module

val androidDataModule = module {
//    single { GestureRecognizerHelperFactory(get()) }
//
//    single<GestureRepository> {
//        GestureRepositoryImpl(
//            context = get(),
//            gestureRecognizerHelperFactory = { runningMode, listener ->
//                get<GestureRecognizerHelperFactory>().create(runningMode, listener)
//            }
//        )
//    }
}
var moduleAndroid = listOf(androidDataModule)
