package org.example.project.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.usecase.gesture.RecognizeGestureUseCase

class GestureViewModel(private val recognizeGestureUseCase: RecognizeGestureUseCase) : ViewModel() {

    private val _gestureResult = MutableStateFlow<GestureResult?>(null)
    val gestureResult: StateFlow<GestureResult?> = _gestureResult.asStateFlow()

    fun onFrameCaptured(frame: Any) {
        viewModelScope.launch {
            recognizeGestureUseCase(frame)
                .collect { result ->
                    _gestureResult.value = result
                }
        }
    }

    fun setup() {
        recognizeGestureUseCase.setup()
    }

    override fun onCleared() {
        super.onCleared()
        recognizeGestureUseCase.close()
    }
}
