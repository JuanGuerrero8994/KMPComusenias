package org.example.project.domain.repository


/**
 * Esta es una interfaz solamente para Desktop
 * porque no tiene una libreria compartida de Firebase para Desktop
 * */
interface FirebaseInitRepository {
    suspend fun initialize(): Boolean
}