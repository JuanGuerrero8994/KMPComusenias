package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.project.ui.viewModel.GestureViewModel
import org.koin.core.module.Module
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoDataOutput
import platform.AVFoundation.AVCaptureVideoDataOutputSampleBufferDelegateProtocol
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.position
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreGraphics.CGRectMake
import platform.CoreImage.CIContext
import platform.CoreImage.CIImage
import platform.CoreImage.createCGImage
import platform.CoreMedia.CMSampleBufferGetImageBuffer
import platform.CoreMedia.CMSampleBufferRef
import platform.CoreVideo.CVPixelBufferGetHeight
import platform.CoreVideo.CVPixelBufferGetWidth
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_queue_create


class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView() {
    val device = AVCaptureDevice.devicesWithMediaType(AVMediaTypeVideo).firstOrNull {
        (it as AVCaptureDevice).position == AVCaptureDevicePositionFront
    } as AVCaptureDevice? ?: return

    val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null) as AVCaptureDeviceInput
    val session = AVCaptureSession().apply {
        sessionPreset = AVCaptureSessionPresetPhoto
        addInput(input)
    }

    val output = AVCaptureVideoDataOutput()
    val queue = dispatch_queue_create("VideoQueue", null)

    val delegate = object : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {
        override fun captureOutput(
            output: AVCaptureOutput,
            didOutputSampleBuffer: CMSampleBufferRef?,
            fromConnection: AVCaptureConnection
        ) {
            if (didOutputSampleBuffer == null) return

            val imageBuffer = CMSampleBufferGetImageBuffer(didOutputSampleBuffer) ?: return
            val ciImage = CIImage(cVPixelBuffer = imageBuffer)
            val context = CIContext()

            val width = CVPixelBufferGetWidth(imageBuffer)
            val height = CVPixelBufferGetHeight(imageBuffer)
            val rect = CGRectMake(0.0, 0.0, width.toDouble(), height.toDouble())

            val cgImage = context.createCGImage(ciImage, fromRect = rect) ?: return
            //val uiImage = UIImage(cgImage)

            //val proxy = PlatformImageProxy(uiImage)
           // gestureViewModel.startLiveStreamRecognition()
        }
    }

    output.setSampleBufferDelegate(delegate, queue)


    session.addOutput(output)

    val cameraPreviewLayer = remember { AVCaptureVideoPreviewLayer(session = session) }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        background = Color.Black,
        factory = {
            object : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {
                override fun layoutSubviews() {
                    super.layoutSubviews()
                    CATransaction.begin()
                    CATransaction.setValue(true, kCATransactionDisableActions)
                    cameraPreviewLayer.frame = this.bounds
                    CATransaction.commit()
                }
            }.apply {
                layer.addSublayer(cameraPreviewLayer)
                cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
                session.startRunning()
            }
        },
        onRelease = {
            session.stopRunning()
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraViewWithPermission() {
    var permissionGranted by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
            when (status) {
                AVAuthorizationStatusAuthorized -> permissionGranted = true
                AVAuthorizationStatusNotDetermined -> {
                    AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                        permissionGranted = granted
                    }
                }
                else -> permissionGranted = false
            }
        }
    }

    when (permissionGranted) {
        null -> {
            // Cargando o esperando permiso
        }
        true -> {
            CameraView()
        }
        false -> {
            // Mostrar mensaje de permiso denegado
            androidx.compose.material.Text("Permiso de cámara denegado. Activarlo desde Configuración.")
        }
    }
}


