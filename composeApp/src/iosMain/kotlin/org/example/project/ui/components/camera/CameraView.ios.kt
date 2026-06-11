package org.example.project.ui.components.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
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

            override fun captureOutput(
                output: AVCaptureOutput,
                didOutputSampleBuffer: CMSampleBufferRef?,
                fromConnection: AVCaptureConnection
            ) {
                val now = timeSource.markNow()

                if (now - lastFrameMark < 100.milliseconds) {
                    return
                }

                lastFrameMark = now

                didOutputSampleBuffer?.let {
                    onFrameCaptured(it)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        val globalQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)
        val dataOutputQueue = dispatch_queue_create("video_data_output_queue", null)

        dispatch_async(globalQueue) {
            session.beginConfiguration()

            // 1. Configurar Cámara Frontal
            val device = AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
                deviceTypes = listOf(AVCaptureDeviceTypeBuiltInWideAngleCamera),
                mediaType = AVMediaTypeVideo,
                position = AVCaptureDevicePositionFront
            ).devices.firstOrNull() as? AVCaptureDevice

            device?.let {
                val input = AVCaptureDeviceInput.deviceInputWithDevice(it, null) as? AVCaptureDeviceInput
                if (input != null && session.canAddInput(input)) {
                    session.addInput(input)
                }
            }

            // 2. Configurar Salida de Datos para MediaPipe
            val videoDataOutput = AVCaptureVideoDataOutput().apply {
                alwaysDiscardsLateVideoFrames = true
                setSampleBufferDelegate(dataOutputDelegate, dataOutputQueue)
            }
            if (session.canAddOutput(videoDataOutput)) {
                session.addOutput(videoDataOutput)
            }

            session.commitConfiguration()
            session.startRunning()
        }

        onDispose {
            dispatch_async(globalQueue) {
                if (session.isRunning()) session.stopRunning()
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