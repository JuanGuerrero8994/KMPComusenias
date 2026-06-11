package org.example.project.data.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.data.mapper.toDomain
import org.example.project.domain.model.mediapipe.GestureResult
import org.example.project.domain.repository.GestureRepository

class GestureRepositoryImpl(private val context: Context) : GestureRepository {
    private var recognizer: GestureRecognizer? = null
    private val _gestureResults = MutableStateFlow<GestureResult?>(null)

    override fun setup() {
        try {
            val options = GestureRecognizer.GestureRecognizerOptions.builder()
                .setBaseOptions(
                    BaseOptions.builder()
                        .setModelAssetPath("gesture_recognizer.task")
                        .build()
                )
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result, _ ->
                    Napier.d("MediaPipe result recibido: $result")
                    _gestureResults.value = result.toDomain()
                }
                .build()

            recognizer = GestureRecognizer.createFromOptions(context, options)

            Napier.d("GestureRecognizer inicializado correctamente")

        } catch (e: Exception) {
            Napier.e("Error inicializando GestureRecognizer", e)
        }
    }

    override fun detectGestures(frame: Any): Flow<GestureResult?> {
        val imageProxy = frame as? ImageProxy

        if (imageProxy == null) {
            Napier.w("Frame inválido recibido: ${frame::class.simpleName}")
            return _gestureResults
        }

        try {
            Napier.d("Procesando frame timestamp=${imageProxy.imageInfo.timestamp}")

            val bitmap: Bitmap = imageProxy.toBitmap()
            val mpImage: MPImage = BitmapImageBuilder(bitmap).build()

            recognizer?.recognizeAsync(mpImage, imageProxy.imageInfo.timestamp)
                ?: Napier.w("Recognizer aún no inicializado")

        } catch (e: Exception) {
            Napier.e("Error en detectGestures()", e)
        }

        return _gestureResults
    }


    override fun close() {
        Napier.d("Cerrando GestureRecognizer")

        try {
            recognizer?.close()
        } catch (e: Exception) {
            Napier.e("Error cerrando recognizer", e)
        }

        recognizer = null
    }
}
