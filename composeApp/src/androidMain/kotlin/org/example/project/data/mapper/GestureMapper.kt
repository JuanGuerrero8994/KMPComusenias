package org.example.project.data.mapper


import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.model.mediapipe.HandLandmark

fun GestureRecognizerResult.toDomain(): GestureResult {
    val topGesture = gestures().firstOrNull()?.firstOrNull()
    val gestureName = topGesture?.categoryName() ?: "None"
    val confidence = topGesture?.score() ?: 0f

    val domainLandmarks = landmarks().map { hand ->
        hand.map { landmark ->
            HandLandmark(
                x = landmark.x(),
                y = landmark.y(),
                z = landmark.z()
            )
        }
    }

    return GestureResult(
        gestureName = gestureName,
        confidence = confidence,
        handLandmarks = domainLandmarks
    )
}
