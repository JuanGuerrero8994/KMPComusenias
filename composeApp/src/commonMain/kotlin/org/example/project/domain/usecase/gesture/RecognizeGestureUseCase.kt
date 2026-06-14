package org.example.project.domain.usecase.gesture

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.repository.GestureRepository

class RecognizeGestureUseCase(private val repository: GestureRepository) {
    operator fun invoke(frame: Any): Flow<GestureResult?> {
        return repository.detectGestures(frame)
    }

    fun setup() {
        repository.setup()
    }

    fun close() {
        repository.close()
    }
}
