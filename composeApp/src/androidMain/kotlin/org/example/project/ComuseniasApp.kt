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
        initLogger()
        startKoin {
            modules(sharedModules)
        }

    }
}