package org.example.project.domain.model.mediapipe

data class HandLandmark(val x: Float, val y: Float, val z: Float)

data class GestureResult(
    val gestureName: String,
    val confidence: Float,
    val handLandmarks: List<List<HandLandmark>> = emptyList()
)