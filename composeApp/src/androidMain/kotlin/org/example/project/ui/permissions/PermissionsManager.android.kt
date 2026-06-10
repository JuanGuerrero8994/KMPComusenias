package org.example.project.ui.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class AndroidPermissionsManager : PermissionsManager {
    @Composable
    override fun askPermission(permission: PermissionType, callback: (PermissionState) -> Unit) {
        val context = LocalContext.current
        val androidPermission = when (permission) {
            PermissionType.CAMERA -> Manifest.permission.CAMERA
            PermissionType.RECORD_AUDIO -> Manifest.permission.RECORD_AUDIO
        }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            callback(if (isGranted) PermissionState.GRANTED else PermissionState.DENIED)
        }

        SideEffect {
            when {
                ContextCompat.checkSelfPermission(context, androidPermission) == PackageManager.PERMISSION_GRANTED -> {
                    callback(PermissionState.GRANTED)
                }
                else -> {
                    launcher.launch(androidPermission)
                }
            }
        }
    }

    override fun isPermissionGranted(permission: PermissionType): Boolean {
        // En Android necesitamos el contexto. Esta es una implementación simplificada.
        return false
    }
}

@Composable
actual fun createPermissionsManager(): PermissionsManager = AndroidPermissionsManager()
