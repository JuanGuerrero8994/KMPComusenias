package org.example.project.ui.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.AVFoundation.*
import platform.Foundation.*

class IOSPermissionsManager : PermissionsManager {
    @Composable
    override fun askPermission(permission: PermissionType, callback: (PermissionState) -> Unit) {
        LaunchedEffect(Unit) {
            val mediaType = when (permission) {
                PermissionType.CAMERA -> AVMediaTypeVideo
                PermissionType.RECORD_AUDIO -> AVMediaTypeAudio
            }
            
            val status = AVCaptureDevice.authorizationStatusForMediaType(mediaType)
            when (status) {
                AVAuthorizationStatusAuthorized -> callback(PermissionState.GRANTED)
                AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> callback(PermissionState.DENIED)
                AVAuthorizationStatusNotDetermined -> {
                    AVCaptureDevice.requestAccessForMediaType(mediaType) { granted ->
                        callback(if (granted) PermissionState.GRANTED else PermissionState.DENIED)
                    }
                }
            }
        }
    }

    override fun isPermissionGranted(permission: PermissionType): Boolean {
        val mediaType = when (permission) {
            PermissionType.CAMERA -> AVMediaTypeVideo
            PermissionType.RECORD_AUDIO -> AVMediaTypeAudio
        }
        return AVCaptureDevice.authorizationStatusForMediaType(mediaType) == AVAuthorizationStatusAuthorized
    }
}

@Composable
actual fun createPermissionsManager(): PermissionsManager = IOSPermissionsManager()
