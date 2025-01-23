package org.example.project

import android.app.Application
import com.google.firebase.FirebaseApp
import org.example.project.di.initKoin
import org.example.project.di.sharedModules
import org.koin.core.context.startKoin


class ComuseniasApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            //androidLogger() // Activa logs
            //androidContext(this@ComuseniasApp) // Contexto para Android
            modules(sharedModules)
        }

    }
}