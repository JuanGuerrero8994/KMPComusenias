package org.example.project.ui.screen.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.example.project.ui.base.HandleResourceState
import org.example.project.ui.components.camera.CameraView
import org.example.project.ui.components.scaffold.ScaffoldComponent
import org.example.project.ui.navigation.Destinations
import org.example.project.ui.permissions.PermissionState
import org.example.project.ui.permissions.PermissionType
import org.example.project.ui.permissions.createPermissionsManager
import org.example.project.ui.screen.auth.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun CameraPreviewScreen(navController:NavController, viewModel: AuthViewModel = koinViewModel()) {

    val signOutResult by viewModel.signOutResult.collectAsState()
    val isLoading = viewModel.loading.collectAsState()

    ScaffoldComponent(
        navController = navController,
        showTopBar = true,
        showBottomBar = true,
        onLogout = { viewModel.signOut() },
        floatingActionButton = null
    ) {
        val permissionsManager = createPermissionsManager()
        var cameraPermissionState by remember { mutableStateOf(PermissionState.NOT_DETERMINED) }
        var audioPermissionState by remember { mutableStateOf(PermissionState.NOT_DETERMINED) }
        var showCamera by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (showCamera) {
                CameraView(
                    modifier = Modifier.fillMaxSize(),
                    onCapture = { data ->
                        // Manejar la captura aquí
                        showCamera = false
                    }
                )
            } else {
                if (cameraPermissionState == PermissionState.GRANTED && audioPermissionState == PermissionState.GRANTED) {
                    Button(onClick = { showCamera = true }) {
                        Text("Abrir Cámara")
                    }
                } else {
                    // Pedir permiso de cámara
                    if (cameraPermissionState != PermissionState.GRANTED) {
                        permissionsManager.askPermission(PermissionType.CAMERA) { state ->
                            cameraPermissionState = state
                        }
                    }

                    // Pedir permiso de audio si el de cámara fue otorgado o ya lo teníamos
                    if (cameraPermissionState == PermissionState.GRANTED && audioPermissionState != PermissionState.GRANTED) {
                        permissionsManager.askPermission(PermissionType.RECORD_AUDIO) { state ->
                            audioPermissionState = state
                        }
                    }

                    if (cameraPermissionState == PermissionState.DENIED || audioPermissionState == PermissionState.DENIED) {
                        Text("Permisos denegados (Cámara o Audio)")
                    } else {
                        Text("Solicitando permisos...")
                    }
                }
            }
            HandleResourceState(
                resource = signOutResult,
                isLoading = isLoading,
                onSuccess = { navController.navigate(Destinations.SplashScreen.route) {
                    popUpTo(0) { inclusive = true }// Limpia el back stack
                } },
            )
        }
    }
}
