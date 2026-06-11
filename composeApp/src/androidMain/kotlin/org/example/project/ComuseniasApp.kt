package org.example.project

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ComuseniasApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initLogger()
        startKoin {
            // Inyectamos el contexto de Android (requiere koin-android)
            androidContext(this@ComuseniasApp)
        }

    }
}