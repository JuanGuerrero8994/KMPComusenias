package org.example.project.data.core

import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException


// Paquete: core
class ErrorHandler {

    /**
     * Función para manejar excepciones y retornarlas como mensajes comprensibles o excepciones custom.
     */
    fun getError(throwable: Throwable): AppError {
        return when (throwable) {

            // Errores de Firebase
            is FirebaseAuthInvalidUserException -> AppError.UserNotFoundError
            is FirebaseAuthInvalidCredentialsException -> AppError.InvalidCredentialsError
            is FirebaseAuthUserCollisionException -> AppError.UserAlreadyExistsError


            // Errores generales (o no manejados)
            else -> AppError.UnknownError
        }
    }
}

/**
 * Clase sellada para definir distintos tipos de errores que la app puede manejar.
 */
sealed class AppError(val message: String) {
    data object UserNotFoundError : AppError("Usuario no encontrado.")
    data object InvalidCredentialsError : AppError("Las credenciales ingresadas son incorrectas.")
    data object UserAlreadyExistsError : AppError("El usuario ya existe.")
    data object UnknownError : AppError("Ocurrió un error desconocido.")
}