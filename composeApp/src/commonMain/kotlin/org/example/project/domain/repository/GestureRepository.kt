package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.core.Resource
import org.example.project.domain.model.gesture.GestureRecognitionResult


interface GestureRepository {

    suspend fun startLiveStreamRecognition(): Flow<Resource<GestureRecognitionResult>>
    suspend fun recognizeImage(imageData: ByteArray): GestureRecognitionResult?
    suspend fun recognizeVideoFile(videoData: ByteArray, inferenceIntervalMs: Long): Flow<Resource<GestureRecognitionResult>>
    fun stopRecognition()
    fun setRecognitionParameters(minHandDetectionConfidence: Float, minHandTrackingConfidence: Float, minHandPresenceConfidence: Float, delegate: RecognitionDelegate)
    suspend fun processLiveStreamFrame(imageProxy: Any): Flow<Resource<GestureRecognitionResult>>
}

enum class RecognitionDelegate {
    CPU,
    GPU
}
