package org.example.project.ui.components.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.CoreGraphics.CGRectMake
import platform.CoreMedia.CMSampleBufferRef
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.NSObject
import platform.darwin.dispatch_queue_create
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView(
    modifier: Modifier,
    onCapture: (ByteArray?) -> Unit,
    onFrameCaptured: (Any) -> Unit
) {
    val session = remember { AVCaptureSession() }
    val cameraPreviewLayer = remember { AVCaptureVideoPreviewLayer(session = session) }

    // Delegate para capturar los frames de video
    val dataOutputDelegate = remember {
        object : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {

            private val timeSource = TimeSource.Monotonic
            private var lastFrameMark = timeSource.markNow()

            init {
                Napier.d(tag = "CameraView", message = "Delegate creado")
            }

            override fun captureOutput(
                output: AVCaptureOutput,
                didOutputSampleBuffer: CMSampleBufferRef?,
                fromConnection: AVCaptureConnection
            ) {

                Napier.d(
                    tag = "CameraView",
                    message = "captureOutput() llamado"
                )

                val now = timeSource.markNow()

                if (now - lastFrameMark < 100.milliseconds) {
                    return
                }

                lastFrameMark = now

                if (didOutputSampleBuffer == null) {
                    Napier.w(
                        tag = "CameraView",
                        message = "SampleBuffer nulo"
                    )
                    return
                }

                Napier.d(
                    tag = "CameraView",
                    message = "Frame enviado al ViewModel"
                )

                onFrameCaptured(didOutputSampleBuffer)
            }
        }
    }

    DisposableEffect(Unit) {

        Napier.d(
            tag = "CameraView",
            message = "Inicializando cámara"
        )

        val globalQueue =
            dispatch_get_global_queue(
                DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(),
                0u
            )

        val dataOutputQueue =
            dispatch_queue_create(
                "video_data_output_queue",
                null
            )

        dispatch_async(globalQueue) {

            Napier.d(
                tag = "CameraView",
                message = "Configurando AVCaptureSession"
            )

            session.beginConfiguration()

            val device =
                AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
                    deviceTypes = listOf(AVCaptureDeviceTypeBuiltInWideAngleCamera),
                    mediaType = AVMediaTypeVideo,
                    position = AVCaptureDevicePositionFront
                ).devices.firstOrNull() as? AVCaptureDevice

            if (device == null) {
                Napier.e(
                    tag = "CameraView",
                    message = "No se encontró cámara frontal"
                )
            } else {
                Napier.d(
                    tag = "CameraView",
                    message = "Cámara encontrada: ${device.localizedName}"
                )
            }

            device?.let {

                val input =
                    AVCaptureDeviceInput.deviceInputWithDevice(
                        it,
                        null
                    ) as? AVCaptureDeviceInput

                if (input != null && session.canAddInput(input)) {

                    Napier.d(
                        tag = "CameraView",
                        message = "Input agregado"
                    )

                    session.addInput(input)

                } else {

                    Napier.e(
                        tag = "CameraView",
                        message = "No se pudo agregar input"
                    )
                }
            }

            val videoDataOutput =
                AVCaptureVideoDataOutput().apply {

                    alwaysDiscardsLateVideoFrames = true

                    setSampleBufferDelegate(
                        dataOutputDelegate,
                        dataOutputQueue
                    )
                }

            if (session.canAddOutput(videoDataOutput)) {

                session.addOutput(videoDataOutput)

                Napier.d(
                    tag = "CameraView",
                    message = "VideoDataOutput agregado"
                )

            } else {

                Napier.e(
                    tag = "CameraView",
                    message = "No se pudo agregar VideoDataOutput"
                )
            }

            session.commitConfiguration()

            Napier.d(
                tag = "CameraView",
                message = "Iniciando sesión"
            )

            session.startRunning()

            Napier.d(
                tag = "CameraView",
                message = "¿Sesión corriendo?: ${session.isRunning()}"
            )
        }

        onDispose {

            Napier.d(
                tag = "CameraView",
                message = "Destruyendo cámara"
            )

            dispatch_async(globalQueue) {
                if (session.isRunning()) {
                    session.stopRunning()

                    Napier.d(
                        tag = "CameraView",
                        message = "Sesión detenida"
                    )
                }
            }
        }
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        background = Color.Black,
        factory = {
            val container = object : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {
                override fun layoutSubviews() {
                    super.layoutSubviews()
                    CATransaction.begin()
                    CATransaction.setValue(true, kCATransactionDisableActions)
                    cameraPreviewLayer.setFrame(this.bounds)
                    CATransaction.commit()
                }
            }
            cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            container.layer.addSublayer(cameraPreviewLayer)
            container
        }
    )
}