package org.example.project

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import org.example.project.di.initKoin
import org.example.project.di.moduleAndroid
import org.koin.dsl.module

class ComuseniasApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initLogger()

        initKoin(
            platformModules = moduleAndroid + listOf(
                module {
                    single<Context> { this@ComuseniasApp }
                }
            )
        )
    }
}
