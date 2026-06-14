package org.example.project.ui.components.camera

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import org.example.project.domain.model.mediapipe.GestureResult

@Composable
fun GestureOverlay(
    modifier: Modifier = Modifier,
    gestureResult: GestureResult?
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        gestureResult?.handLandmarks?.forEach { hand ->
            // Draw connections between landmarks (simplified)
            // MediaPipe Hand Landmarks have 21 points.
            // Typical connections:
            // Thumb: 0-1, 1-2, 2-3, 3-4
            // Index: 0-5, 5-6, 6-7, 7-8
            // Middle: 0-9, 9-10, 10-11, 11-12
            // Ring: 0-13, 13-14, 14-15, 15-16
            // Pinky: 0-17, 17-18, 18-19, 19-20
            // Palm: 5-9, 9-13, 13-17

            hand.forEach { landmark ->
                drawCircle(
                    color = Color.Green,
                    radius = 8f,
                    center = Offset(landmark.x * width, landmark.y * height)
                )
            }
        }
    }
}
