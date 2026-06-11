package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.mediapipe.GestureResult

interface GestureRepository {
    // Procesa un frame individual y devuelve el resultado
    fun detectGestures(frame: Any): Flow<GestureResult?>

    // Configuración inicial del motor (opcional)
    fun setup()

    // Cierra recursos
    fun close()
}