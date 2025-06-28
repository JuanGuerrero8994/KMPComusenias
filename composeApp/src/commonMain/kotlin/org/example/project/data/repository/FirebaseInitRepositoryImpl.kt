package org.example.project.data.repository

// jvmMain (solo para Desktop)

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.example.project.domain.repository.FirebaseInitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.project.createHttpClient

class FirebaseInitRepositoryImpl : FirebaseInitRepository {
    private val httpClient: HttpClient by lazy { createHttpClient() }

    override suspend fun initialize(): Boolean = withContext(Dispatchers.Default) {
        val url = "https://firestore.googleapis.com/v1/projects/fir-kmm-98510/databases/(default)/documents/USERS"

        return@withContext try {
            val response: HttpResponse = httpClient.get(url) {
                accept(ContentType.Application.Json)
            }
            Napier.d("✅ Firebase conectado en Desktop. Código HTTP: ${response.status}")
            response.status.isSuccess()
        } catch (e: Exception) {
            Napier.e("❌ Error en Desktop FirebaseInit: ${e.message}")
            false
        }
    }
}
