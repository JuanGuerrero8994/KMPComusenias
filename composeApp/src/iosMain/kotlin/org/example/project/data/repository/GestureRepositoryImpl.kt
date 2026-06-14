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
import platform.Foundation.NSDate
import platform.Foundation.NSError
import platform.Foundation.timeIntervalSince1970
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

                Napier.d(
                    tag = "MediaPipe",
                    message = "Delegate llamado timestamp=$timestampInMilliseconds"
                )

                error?.let {
                    Napier.e(
                        tag = "MediaPipe",
                        message = "Delegate error: ${it.localizedDescription}"
                    )
                    return
                }

                if (didFinishRecognitionWithResult == null) {
                    Napier.w(
                        tag = "MediaPipe",
                        message = "Resultado nulo"
                    )
                    return
                }

                Napier.d(
                    tag = "MediaPipe",
                    message = "Resultado recibido: $didFinishRecognitionWithResult"
                )

                _results.value = didFinishRecognitionWithResult.toDomain()
            }
        }

    override fun setup() {

        Napier.d(
            tag = "MediaPipe",
            message = "Iniciando GestureRecognizer"
        )

        try {

            val modelPath = NSBundle.mainBundle.pathForResource(
                name = "gesture_recognizer",
                ofType = "task"
            )

            if (modelPath == null) {
                Napier.e(
                    tag = "MediaPipe",
                    message = "No se encontró gesture_recognizer.task en el bundle"
                )
                return
            }

            Napier.d(
                tag = "MediaPipe",
                message = "Modelo encontrado: $modelPath"
            )

            val options = MPPGestureRecognizerOptions().apply {

                runningMode = MPPRunningMode.MPPRunningModeLiveStream

                gestureRecognizerLiveStreamDelegate = mediapipeDelegate

                numHands = 2

                baseOptions.modelAssetPath = modelPath
            }

            recognizer =
                MPPGestureRecognizer(
                    options = options,
                    error = null
                )

            if (recognizer == null) {
                Napier.e(
                    tag = "MediaPipe",
                    message = "No se pudo crear el recognizer"
                )
                return
            }

            Napier.d(
                tag = "MediaPipe",
                message = "GestureRecognizer inicializado correctamente"
            )

        } catch (e: Exception) {

            Napier.e(
                tag = "MediaPipe",
                message = "Error inicializando MediaPipe",
                throwable = e
            )
        }
    }

    override fun detectGestures(frame: Any): Flow<GestureResult?> {
        Napier.d(tag = "MediaPipe", message = "detectGestures() llamado")

        val sampleBuffer = frame as? CMSampleBufferRef

        if (sampleBuffer == null) {

            Napier.w(
                tag = "MediaPipe",
                message = "Frame inválido recibido"
            )

            return _results
        }

        val recognizerInstance = recognizer

        if (recognizerInstance == null) {

            Napier.w(
                tag = "MediaPipe",
                message = "Recognizer no inicializado"
            )

            return _results
        }

        try {

            val timestampMs =
                (NSDate().timeIntervalSince1970 * 1000).toLong()

            Napier.d(
                tag = "MediaPipe",
                message = "Procesando frame timestamp=$timestampMs"
            )

            val image = MPPImage(
                sampleBuffer = sampleBuffer,
                orientation = UIImageOrientation.UIImageOrientationUp,
                error = null
            )

            recognizerInstance.recognizeAsyncImage(
                image = image,
                timestampInMilliseconds = timestampMs,
                error = null
            )

        } catch (e: Exception) {

            Napier.e(
                tag = "MediaPipe",
                message = "Error procesando frame",
                throwable = e
            )
        }

        return _results
    }

    override fun close() {

        Napier.d(
            tag = "MediaPipe",
            message = "Cerrando GestureRecognizer"
        )

        recognizer = null
    }
}