package org.example.project.ui.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.project.data.core.Resource
import org.example.project.domain.model.gesture.GestureRecognitionResult
import org.example.project.domain.repository.RecognitionDelegate
import org.example.project.domain.usecase.gesture.GestureAction
import org.example.project.domain.usecase.gesture.GestureUseCase // Import your consolidated UseCase
import org.example.project.ui.base.BaseViewModel // Assuming BaseViewModel is in this package structure

// Import specific GestureAction types for cleaner calls
import org.example.project.domain.usecase.gesture.GestureAction.RecognizeImage
import org.example.project.domain.usecase.gesture.GestureAction.RecognizeVideo
import org.example.project.domain.usecase.gesture.GestureAction.SetRecognitionParameters
import org.example.project.domain.usecase.gesture.GestureAction.StartLiveStreamRecognition
import org.example.project.domain.usecase.gesture.GestureAction.StopRecognition


class GestureViewModel(
    private val useCase: GestureUseCase,
) : BaseViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _gestureRecognitionResult = MutableStateFlow<Resource<GestureRecognitionResult>>(Resource.Loading)
    val gestureRecognitionResult: StateFlow<Resource<GestureRecognitionResult>> get() = _gestureRecognitionResult

    fun startLiveStreamRecognition() {
        _loading.value = true // Set loading before the operation
        fetchData(_gestureRecognitionResult) {
            useCase.invoke(StartLiveStreamRecognition) // This returns Flow<Resource<GestureRecognitionResult>>
        }
    }

    fun stopRecognition() {
        _loading.value = true // Indicates an operation is in progress (stopping)
        fetchData(_gestureRecognitionResult) {
            // Call the UseCase with the StopRecognition action
            // This also returns Flow<Resource<GestureRecognitionResult>> (with an empty result for success)
            useCase.invoke(StopRecognition)
        }
    }

    fun recognizeImage(imageData: ByteArray) {
        _loading.value = true
        fetchData(_gestureRecognitionResult) {
            // Pass the data using the data class within the sealed interface
            useCase.invoke(RecognizeImage(imageData))
        }
    }

    fun recognizeVideo(videoData: ByteArray, inferenceIntervalMs: Long) {
        _loading.value = true
        fetchData(_gestureRecognitionResult) {
            // Pass the data using the data class within the sealed interface
            useCase.invoke(RecognizeVideo(videoData, inferenceIntervalMs))
        }
    }

    fun setRecognitionParameters(
        minHandDetectionConfidence: Float,
        minHandTrackingConfidence: Float,
        minHandPresenceConfidence: Float,
        delegate: RecognitionDelegate
    ) {
        _loading.value = true
        fetchData(_gestureRecognitionResult) {
            // Pass all parameters within the SetRecognitionParameters data class
            useCase.invoke(
                SetRecognitionParameters(
                    minHandDetectionConfidence,
                    minHandTrackingConfidence,
                    minHandPresenceConfidence,
                    delegate
                )
            )
        }
    }

    // You might also want a generic reset function for the result state
    fun resetGestureResultState() {
        _gestureRecognitionResult.value = Resource.Loading
    }

    fun processLiveStreamFrame(imageProxy: Any) {
        // No ponemos loading porque es un flujo continuo de frames, no una carga puntual
        // La lógica para emitir resultados está en el flow de startLiveStreamRecognition
        fetchData(_gestureRecognitionResult) {
            useCase.invoke(GestureAction.AnalyzeLiveStreamFrame(imageProxy))
        }
    }



}