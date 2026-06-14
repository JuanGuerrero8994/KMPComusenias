package org.example.project

import android.app.Application
import com.google.firebase.FirebaseApp
import org.example.project.di.initKoin
import org.koin.android.ext.koin.androidContext


class ComuseniasApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initKoin {
            // Inyectamos el contexto de Android (requiere koin-android)
            androidContext(this@ComuseniasApp)
        }

    }
}