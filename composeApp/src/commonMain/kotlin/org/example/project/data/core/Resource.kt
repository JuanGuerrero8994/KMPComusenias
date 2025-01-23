package org.example.project.data.core
/*
// Clase para manejar los estados de éxito, error y carga

*/
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}