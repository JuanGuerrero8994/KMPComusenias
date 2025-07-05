package org.example.project.domain.model.gesture


/**
 * Represents the complete result of a single gesture recognition inference.
 *
 * @property detectedHands A list of hands detected in the input image/frame,
 * each with its recognized gesture and landmark coordinates.
 * @property inferenceTimeMs The time taken for the inference in milliseconds.
 * @property inputImageWidth The width of the input image/frame that was processed.
 * @property inputImageHeight The height of the input image/frame that was processed.
 */
data class GestureRecognitionResult(
    val detectedHands: List<DetectedHand>, // Renamed from 'gestures' for clarity
    val inferenceTimeMs: Long,
    val inputImageWidth: Int,
    val inputImageHeight: Int
)

/**
 * Represents a single hand detected in the input, including its recognized gesture
 * and the coordinates of its landmarks.
 *
 * @property gestureName The name of the most prominent gesture recognized for this hand (e.g., "Open_Palm", "Thumb_Up").
 * @property landmarks A list of 3D points representing the key joints (landmarks) of the hand.
 * Coordinates are typically normalized (0.0 to 1.0).
 */
data class DetectedHand(
    val gestureName: String,
    val landmarks: List<PointF>,
)

/**
 * Represents a 3D point with normalized coordinates.
 *
 * @property x The X-coordinate, typically normalized between 0.0 and 1.0.
 * @property y The Y-coordinate, typically normalized between 0.0 and 1.0.
 * @property z The Z-coordinate, representing depth, typically normalized between 0.0 and 1.0.
 * Can be omitted or default to 0f if only 2D coordinates are needed.
 */
data class PointF(val x: Float, val y: Float, val z: Float = 0f)