package org.example.project.data.mapper

import cocoapods.MediaPipeTasksVision.MPPCategory
import cocoapods.MediaPipeTasksVision.MPPLandmark
import cocoapods.MediaPipeTasksVision.MPPGestureRecognizerResult
import kotlinx.cinterop.ExperimentalForeignApi
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.model.mediapipe.HandLandmark

@OptIn(ExperimentalForeignApi::class)
fun MPPGestureRecognizerResult.toDomain(): GestureResult {
    val firstHandGestures = gestures.firstOrNull() as? List<MPPCategory>
    val topGesture = firstHandGestures?.firstOrNull()

    val gestureName = topGesture?.categoryName ?: "None"
    val confidence = topGesture?.score ?: 0f

    val domainLandmarks = landmarks.map { hand ->
        (hand as List<*>).map { landmark ->
            val l = landmark as MPPLandmark
            HandLandmark(
                x = l.x,
                y = l.y,
                z = l.z
            )
        }
    }

    return GestureResult(
        gestureName = gestureName,
        confidence = confidence,
        handLandmarks = domainLandmarks
    )
}
