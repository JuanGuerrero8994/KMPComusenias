package org.example.project.data.repository

import cocoapods.MediaPipeTasksVision.*
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreMedia.CMSampleBufferRef
import platform.CoreMedia.CMSampleBufferGetPresentationTimeStamp
import platform.CoreMedia.CMTimeGetSeconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.data.mapper.toDomain
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.repository.GestureRepository
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.UIKit.UIImageOrientation
import platform.darwin.NSObject
@OptIn(ExperimentalForeignApi::class)

class GestureRepositoryImpl : GestureRepository {

    private var recognizer: MPPGestureRecognizer? = null
    private val _results = MutableStateFlow<GestureResult?>(null)

    private val mediapipeDelegate =
        object : NSObject(), MPPGestureRecognizerLiveStreamDelegateProtocol {

            override fun gestureRecognizer(
                gestureRecognizer: MPPGestureRecognizer,
                didFinishRecognitionWithResult: MPPGestureRecognizerResult?,
                timestampInMilliseconds: Long,
                error: NSError?
            ) {
                if (error != null) {
                    Napier.e(tag = "MediaPipe", message = error.localizedDescription)
                    return
                }

                didFinishRecognitionWithResult?.let {
                    Napier.d("MediaPipe result recibido: $it")
                    _results.value = it.toDomain()
                }
            }
        }

    override fun setup() {
        Napier.d("GestureRecognizer setup iniciado (iOS)")

        try {
            val options = MPPGestureRecognizerOptions()

            options.runningMode = MPPRunningMode.MPPRunningModeLiveStream
            options.gestureRecognizerLiveStreamDelegate = mediapipeDelegate

            val modelPath = NSBundle.mainBundle.pathForResource(
                "gesture_recognizer",
                "task"
            )

            if (modelPath == null) {
                Napier.e("No se encontró gesture_recognizer.task en el bundle")
                return
            }

            options.baseOptions.modelAssetPath = modelPath

            recognizer = MPPGestureRecognizer(options = options, error = null)

            Napier.d("GestureRecognizer inicializado OK")

        } catch (e: Exception) {
            Napier.e("Error inicializando GestureRecognizer", e)
        }
    }

    override fun detectGestures(frame: Any): Flow<GestureResult?> {

        val buffer = frame as? CMSampleBufferRef

        if (buffer == null) {
            Napier.w("Frame inválido recibido: ${frame::class.simpleName}")
            return _results
        }

        try {
            val presentationTime = CMSampleBufferGetPresentationTimeStamp(buffer)
            val timestampMS = (CMTimeGetSeconds(presentationTime) * 1000).toLong()

            Napier.d("Procesando frame timestamp=$timestampMS")

            val mppImage = MPPImage(
                sampleBuffer = buffer,
                orientation = UIImageOrientation.UIImageOrientationUp,
                error = null
            )

            recognizer?.recognizeAsyncImage(
                image = mppImage,
                timestampInMilliseconds = timestampMS,
                error = null
            ) ?: Napier.w("Recognizer aún no inicializado")

        } catch (e: Exception) {
            Napier.e("Error en detectGestures()", e)
        }

        return _results
    }

    override fun close() {
        Napier.d("Cerrando GestureRecognizer")

        recognizer = null
    }
}