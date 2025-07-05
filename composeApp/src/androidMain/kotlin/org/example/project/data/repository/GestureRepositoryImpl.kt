package org.example.project.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.example.project.data.core.GestureRecognizerHelper
import org.example.project.data.core.Resource
import org.example.project.data.mapper.toGestureRecognitionResult
import org.example.project.domain.model.gesture.GestureRecognitionResult
import org.example.project.domain.repository.GestureRepository
import org.example.project.domain.repository.RecognitionDelegate
import java.io.File

class GestureRepositoryImpl(
    private val context: Context,
    private val gestureRecognizerHelperFactory: (RunningMode, GestureRecognizerHelper.GestureRecognizerListener?) -> GestureRecognizerHelper
) : GestureRepository {

    private var gestureRecognizerHelper: GestureRecognizerHelper? = null

    private fun RecognitionDelegate.toHelperDelegate(): Int {
        return when (this) {
            RecognitionDelegate.CPU -> GestureRecognizerHelper.DELEGATE_CPU
            RecognitionDelegate.GPU -> GestureRecognizerHelper.DELEGATE_GPU
        }
    }

    override fun setRecognitionParameters(
        minHandDetectionConfidence: Float,
        minHandTrackingConfidence: Float,
        minHandPresenceConfidence: Float,
        delegate: RecognitionDelegate
    ) {
        gestureRecognizerHelper?.clearGestureRecognizer()
        gestureRecognizerHelper = gestureRecognizerHelperFactory(
            RunningMode.IMAGE,
            null
        ).apply {
            this.minHandDetectionConfidence = minHandDetectionConfidence
            this.minHandTrackingConfidence = minHandTrackingConfidence
            this.minHandPresenceConfidence = minHandPresenceConfidence
            this.currentDelegate = delegate.toHelperDelegate()
            setupGestureRecognizer()
        }
    }

    override suspend fun processLiveStreamFrame(imageProxy: Any): Flow<Resource<GestureRecognitionResult>> {
        return flow {
            if (gestureRecognizerHelper == null || gestureRecognizerHelper?.runningMode != RunningMode.LIVE_STREAM) {
                gestureRecognizerHelper?.clearGestureRecognizer()
                throw IllegalStateException("LiveStream recognizer not initialized. Call startLiveStreamRecognition() first.")
            }
            gestureRecognizerHelper?.recognizeLiveStream(imageProxy as ImageProxy)
            emit(Resource.Loading) // emitimos Loading porque el resultado llegará asíncronamente en el listener
        }
    }

    override suspend fun startLiveStreamRecognition(): Flow<Resource<GestureRecognitionResult>> = callbackFlow {
        if (gestureRecognizerHelper == null || gestureRecognizerHelper?.runningMode != RunningMode.LIVE_STREAM) {
            gestureRecognizerHelper?.clearGestureRecognizer()
            gestureRecognizerHelper = gestureRecognizerHelperFactory(
                RunningMode.LIVE_STREAM,
                object : GestureRecognizerHelper.GestureRecognizerListener {
                    override fun onError(error: String, errorCode: Int) {
                        launch { send(Resource.Error(RuntimeException(error))) }
                    }

                    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
                        launch { send(Resource.Success(resultBundle.toGestureRecognitionResult())) }
                    }
                }
            )
        }

        awaitClose {
            gestureRecognizerHelper?.clearGestureRecognizer()
            gestureRecognizerHelper = null
        }
    }

    override suspend fun recognizeImage(imageData: ByteArray): GestureRecognitionResult? {
        if (gestureRecognizerHelper?.runningMode != RunningMode.IMAGE || gestureRecognizerHelper == null) {
            gestureRecognizerHelper?.clearGestureRecognizer()
            gestureRecognizerHelper = gestureRecognizerHelperFactory(
                RunningMode.IMAGE,
                null
            )
        }

        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        return gestureRecognizerHelper?.recognizeImage(bitmap)?.toGestureRecognitionResult()
    }

    override suspend fun recognizeVideoFile(videoData: ByteArray, inferenceIntervalMs: Long): Flow<Resource<GestureRecognitionResult>> =
        flow {
            if (gestureRecognizerHelper?.runningMode != RunningMode.VIDEO || gestureRecognizerHelper == null) {
                gestureRecognizerHelper?.clearGestureRecognizer()
                gestureRecognizerHelper = gestureRecognizerHelperFactory(
                    RunningMode.VIDEO,
                    null
                )
            }

            val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
            tempFile.writeBytes(videoData)
            val videoUri = Uri.fromFile(tempFile)

            val resultBundle = gestureRecognizerHelper?.recognizeVideoFile(videoUri, inferenceIntervalMs)

            resultBundle?.let {
                emit(Resource.Success(it.toGestureRecognitionResult()))
            } ?: emit(Resource.Error(Exception("Video recognition failed or returned no results.")))

            tempFile.delete()
        }
            .flowOn(Dispatchers.IO)

    override fun stopRecognition() {
        gestureRecognizerHelper?.clearGestureRecognizer()
        gestureRecognizerHelper = null
    }
}
