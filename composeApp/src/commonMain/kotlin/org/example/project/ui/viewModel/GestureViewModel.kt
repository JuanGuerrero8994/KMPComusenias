package org.example.project.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.usecase.gesture.RecognizeGestureUseCase

class GestureViewModel(private val recognizeGestureUseCase: RecognizeGestureUseCase) : ViewModel() {

    val gestureResult: StateFlow<GestureResult?> = recognizeGestureUseCase(Any())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        setup()
    }

    fun onFrameCaptured(frame: Any) {
        // Ejecutamos para procesar el frame. El resultado se emitirá por el StateFlow.
        recognizeGestureUseCase(frame)
    }

    fun setup() {
        recognizeGestureUseCase.setup()
    }

    override fun onCleared() {
        super.onCleared()
        recognizeGestureUseCase.close()
    }
}
