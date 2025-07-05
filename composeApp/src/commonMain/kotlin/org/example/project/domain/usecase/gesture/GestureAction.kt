package org.example.project.domain.usecase.gesture

import org.example.project.domain.repository.RecognitionDelegate

sealed interface GestureAction { // Use sealed interface if Kotlin version supports (1.5+), else sealed class

    // Actions that don't need parameters are data objects
    data object StartLiveStreamRecognition : GestureAction
    data object StopRecognition : GestureAction // Adding back StopRecognition from previous discussion

    // Actions that need parameters are data classes
    data class RecognizeImage(val imageData: ByteArray) : GestureAction
    data class RecognizeVideo(val videoData: ByteArray, val inferenceIntervalMs: Long) : GestureAction

    data class SetRecognitionParameters(val minHandDetectionConfidence: Float, val minHandTrackingConfidence: Float, val minHandPresenceConfidence: Float, val delegate: RecognitionDelegate) : GestureAction

    data class AnalyzeLiveStreamFrame(val imageProxy: Any) : GestureAction




}