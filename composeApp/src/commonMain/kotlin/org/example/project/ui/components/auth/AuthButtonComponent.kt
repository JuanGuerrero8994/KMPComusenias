package org.example.project.ui.components.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthButton(text: String, onClick: () -> Unit, isEnabled: Boolean = true) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled
    ) {
        Text(text)
    }
}