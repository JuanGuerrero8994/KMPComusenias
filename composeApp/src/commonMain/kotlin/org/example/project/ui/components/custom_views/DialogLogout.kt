package com.example.comuseniaskmm.android.ui.components.custom_views


import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import org.example.project.ui.screen.navigation.Destinations

@Composable
fun DialogLogout(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    navController: NavController

) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Cerrar Sesión")
            },
            text = {
                Text(text = "¿Estás seguro de que quieres cerrar sesión? Puedes seguir jugando si prefieres.")
            },
            confirmButton = {
                Button(onClick = onLogout) {
                    LaunchedEffect(Unit) {
                        navController.navigate(Destinations.AuthScreen.route) {
                            popUpTo(0) // Limpiar la pila de navegación
                        }
                    }
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text(text = "Seguir Jugando")
                }
            }
        )
    }
}