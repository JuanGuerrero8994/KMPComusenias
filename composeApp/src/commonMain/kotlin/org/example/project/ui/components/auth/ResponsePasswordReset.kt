package org.example.project.ui.components.auth

import androidx.compose.runtime.Composable
import kmpcomusenias.composeapp.generated.resources.Res
import kmpcomusenias.composeapp.generated.resources.icon_send_email
import org.example.project.ui.components.defaults.DialogImageTextDefault


@Composable
fun PasswordResetDialog(onDismiss: () -> Unit, onButton: () -> Unit) {
    DialogImageTextDefault(
        icon = Res.drawable.icon_send_email,
        title = "Genial! Estás a un paso",
        text = "Enviamos un email al correo electrónico que nos proporcionaste con instrucciones para cambiar tu contraseña. \n " +
                "Si no aparece en tu bandeja de entrada recuerda revisar la carpeta de spam o no deseados.",
        onDismissRequest = onDismiss,
        onButtonOk = onButton
    )
}




