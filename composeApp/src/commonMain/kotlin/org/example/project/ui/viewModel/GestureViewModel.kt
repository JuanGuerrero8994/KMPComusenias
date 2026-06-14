package org.example.project.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.usecase.gesture.RecognizeGestureUseCase


class GestureViewModel(
    private val recognizeGestureUseCase: RecognizeGestureUseCase
) : ViewModel() {

    private val _gestureResult = MutableStateFlow<GestureResult?>(null)
    val gestureResult: StateFlow<GestureResult?> = _gestureResult.asStateFlow()

    init {
        setup()
    }

    fun onFrameCaptured(frame: Any) {
        Napier.d(tag = "GestureVM", message = "Frame recibido desde CameraView")
        viewModelScope.launch {
            recognizeGestureUseCase(frame)
                .collect { result ->
                    _gestureResult.value = result
                }
        }
    }

    private fun setup() {
        recognizeGestureUseCase.setup()
    }

    override fun onCleared() {
        super.onCleared()
        recognizeGestureUseCase.close()
    }
}