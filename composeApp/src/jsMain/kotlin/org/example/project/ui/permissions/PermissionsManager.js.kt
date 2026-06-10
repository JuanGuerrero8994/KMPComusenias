package org.example.project.ui.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.window
import org.w3c.dom.mediacapture.MediaStreamConstraints

class JsPermissionsManager : PermissionsManager {
    @Composable
    override fun askPermission(permission: PermissionType, callback: (PermissionState) -> Unit) {
        LaunchedEffect(Unit) {
            val navigator = window.navigator
            if (navigator.mediaDevices == null) {
                callback(PermissionState.DENIED)
                return@LaunchedEffect
            }

            val constraints = when (permission) {
                PermissionType.CAMERA -> MediaStreamConstraints(video = true)
                PermissionType.RECORD_AUDIO -> MediaStreamConstraints(audio = true)
            }

            navigator.mediaDevices.getUserMedia(constraints)
                .then {
                    callback(PermissionState.GRANTED)
                }
                .catch {
                    callback(PermissionState.DENIED)
                }
        }
    }

    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return false
    }
}

@Composable
actual fun createPermissionsManager(): PermissionsManager = JsPermissionsManager()
