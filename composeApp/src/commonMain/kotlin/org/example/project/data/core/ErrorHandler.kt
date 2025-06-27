package org.example.project.data.core

import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import io.github.aakira.napier.Napier

class ErrorHandler {

    fun getError(throwable: Throwable): AppError {
        val appError = when (throwable) {
            is FirebaseAuthInvalidUserException -> AppError.UserNotFoundError
            is FirebaseAuthInvalidCredentialsException -> AppError.InvalidCredentialsError
            is FirebaseAuthUserCollisionException -> AppError.UserAlreadyExistsError
            else -> AppError.UnknownError
        }
        Napier.e("❌ ${appError.message} | Firebase: ${throwable.message}", throwable)
        return appError
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
