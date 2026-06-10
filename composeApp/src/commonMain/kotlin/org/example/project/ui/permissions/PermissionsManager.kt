package org.example.project.ui.permissions

import androidx.compose.runtime.Composable

enum class PermissionType {
    CAMERA,
    RECORD_AUDIO
}

enum class PermissionState {
    GRANTED,
    DENIED,
    NOT_DETERMINED
}

interface PermissionsManager {
    @Composable
    fun askPermission(permission: PermissionType, callback: (PermissionState) -> Unit)
    
    fun isPermissionGranted(permission: PermissionType): Boolean
}

@Composable
expect fun createPermissionsManager(): PermissionsManager
