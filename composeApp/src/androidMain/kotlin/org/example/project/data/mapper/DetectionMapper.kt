package org.example.project.data.mapper

import org.example.project.data.core.GestureRecognizerHelper
import org.example.project.domain.model.gesture.DetectedHand
import org.example.project.domain.model.gesture.GestureRecognitionResult
import org.example.project.domain.model.gesture.PointF

fun GestureRecognizerHelper.ResultBundle.toGestureRecognitionResult(): GestureRecognitionResult {
    val detectedHandsList = results.flatMap { gestureRecognizerResult ->
        // Cada result puede tener varios landmarks y gestos
        gestureRecognizerResult.landmarks().mapIndexed { index, mpLandmarks ->
            val mpGestures = gestureRecognizerResult.gestures().getOrNull(index)

            val domainLandmarks = mpLandmarks.map { mpLandmark ->
                PointF(mpLandmark.x(), mpLandmark.y(), mpLandmark.z())
            }

            val gestureName = mpGestures?.firstOrNull()?.categoryName() ?: "Unknown"

            DetectedHand(
                gestureName = gestureName,
                landmarks = domainLandmarks
            )
        }
    }

    return GestureRecognitionResult(
        detectedHands = detectedHandsList,
        inferenceTimeMs = inferenceTime,
        inputImageWidth = inputImageWidth,
        inputImageHeight = inputImageHeight
    )
}
