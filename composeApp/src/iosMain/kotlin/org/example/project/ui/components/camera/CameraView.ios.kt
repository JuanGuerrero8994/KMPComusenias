package org.example.project.ui.components.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView(
    modifier: Modifier,
    onCapture: (ByteArray?) -> Unit
) {
    // 1. Usamos remember para que la sesión no se recree en cada recomposición
    val session = remember { AVCaptureSession() }
    val cameraPreviewLayer = remember { AVCaptureVideoPreviewLayer(session = session) }

    // 2. Gestionamos el inicio y parada de la sesión según el ciclo de vida del componente
    DisposableEffect(Unit) {
        val globalQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)

        dispatch_async(globalQueue) {
            session.beginConfiguration()

            // Configuración de la cámara frontal (Usando API moderna)
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

            if (session.canSetSessionPreset(AVCaptureSessionPresetHigh)) {
                session.sessionPreset = AVCaptureSessionPresetHigh
            }

            session.commitConfiguration()
            session.startRunning()
        }

        onDispose {
            dispatch_async(globalQueue) {
                if (session.isRunning()) {
                    session.stopRunning()
                }
            }
        }
    }

    // 3. UI con UIKitView
    UIKitView(
        modifier = modifier.fillMaxSize(),
        background = Color.Black,
        factory = {
            // Creamos un contenedor que ajuste la capa de la cámara automáticamente
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