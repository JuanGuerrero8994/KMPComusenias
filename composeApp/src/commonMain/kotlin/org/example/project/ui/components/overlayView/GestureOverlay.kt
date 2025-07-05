package org.example.project.ui.components.overlayView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.domain.model.gesture.GestureRecognitionResult
import org.example.project.domain.model.gesture.PointF



@Composable
fun GestureOverlay(
    result: GestureRecognitionResult?,
    modifier: Modifier = Modifier,
    shouldMirrorX: Boolean = true
) {
    if (result == null || result.detectedHands.isEmpty()) return

    val density = LocalDensity.current
    val landmarkRadius = with(density) { 4.dp.toPx() }
    val strokeWidth = with(density) { 2.dp.toPx() }

    Box(modifier = modifier.fillMaxSize()) {

        // Draw hand landmarks and connections
        Canvas(modifier = Modifier.fillMaxSize()) {

            val canvasWidth = size.width
            val canvasHeight = size.height
            val imageWidth = result.inputImageWidth.toFloat()
            val imageHeight = result.inputImageHeight.toFloat()
            val scaleFactor = minOf(canvasWidth / imageWidth, canvasHeight / imageHeight)
            val scaledImageWidth = imageWidth * scaleFactor
            val scaledImageHeight = imageHeight * scaleFactor
            val xOffset = (canvasWidth - scaledImageWidth) / 2f
            val yOffset = (canvasHeight - scaledImageHeight) / 2f

            val handConnections = listOf(
                0 to 1, 1 to 2, 2 to 3, 3 to 4,
                0 to 5, 5 to 6, 6 to 7, 7 to 8,
                5 to 9, 9 to 10, 10 to 11, 11 to 12,
                9 to 13, 13 to 14, 14 to 15, 15 to 16,
                13 to 17, 17 to 18, 18 to 19, 19 to 20,
                0 to 17
            )

            result.detectedHands.forEach { hand ->

                // Draw hand connections
                handConnections.forEach { (start, end) ->
                    if (start < hand.landmarks.size && end < hand.landmarks.size) {
                        val startPt = hand.landmarks[start].toOffset(scaledImageWidth, scaledImageHeight, xOffset, yOffset, shouldMirrorX)
                        val endPt = hand.landmarks[end].toOffset(scaledImageWidth, scaledImageHeight, xOffset, yOffset, shouldMirrorX)
                        drawLine(
                            color = Color.Blue,
                            start = startPt,
                            end = endPt,
                            strokeWidth = strokeWidth
                        )
                    }
                }

                // Draw landmarks
                hand.landmarks.forEach { landmark ->
                    val offset = landmark.toOffset(scaledImageWidth, scaledImageHeight, xOffset, yOffset, shouldMirrorX)
                    drawCircle(
                        color = Color.Red,
                        radius = landmarkRadius,
                        center = offset
                    )
                }
            }
        }

        // Draw gesture name text at the wrist position (landmark 0)
        result.detectedHands.forEach { hand ->
            val wrist = hand.landmarks.getOrNull(0)
            wrist?.let {
                val xPositionPercent = if (shouldMirrorX) 1f - it.x else it.x
                val yPositionPercent = it.y

                // Approximate canvas size to dp (adjust as needed)
                val canvasWidthDp: Dp = 300.dp
                val canvasHeightDp: Dp = 400.dp

                val xOffsetDp = canvasWidthDp * xPositionPercent
                val yOffsetDp = canvasHeightDp * yPositionPercent

                Text(
                    text = hand.gestureName,
                    color = Color.Green,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = xOffsetDp - 20.dp, y = yOffsetDp - 20.dp) // adjust for better centering
                )
            }
        }
    }
}

// Helper extension function to convert normalized PointF to canvas Offset
private fun PointF.toOffset(
    scaledWidth: Float,
    scaledHeight: Float,
    xOffset: Float,
    yOffset: Float,
    mirrorX: Boolean
): Offset {
    val x = if (mirrorX) (1f - this.x) * scaledWidth + xOffset else this.x * scaledWidth + xOffset
    val y = this.y * scaledHeight + yOffset
    return Offset(x, y)
}


@Composable
fun GestureOverlayWithText(
    result: GestureRecognitionResult?,
    modifier: Modifier = Modifier,
    shouldMirrorX: Boolean = true
) {
    if (result == null || result.detectedHands.isEmpty()) return

    Box(modifier = modifier.fillMaxSize()) {
        // Dibuja la malla y los landmarks
        GestureOverlay(result = result, modifier = Modifier.fillMaxSize(), shouldMirrorX = shouldMirrorX)

        // Dibuja el texto del gesto arriba de la muñeca
        result.detectedHands.forEach { hand ->
            val wrist = hand.landmarks.getOrNull(0)
            wrist?.let {
                val posX = if (shouldMirrorX) 1f - it.x else it.x
                val posY = it.y

                // Posicionamiento relativo al tamaño de la pantalla
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = hand.gestureName,
                        color = Color.Green,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                            .offset(
                                x = (posX * 300).dp, // Ajustá el 300 por el ancho del canvas aproximado
                                y = (posY * 500).dp  // Ajustá el 500 por el alto del canvas aproximado
                            )
                    )
                }
            }
        }
    }
}
