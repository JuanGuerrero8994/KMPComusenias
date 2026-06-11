package org.example.project.ui.components.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CameraView(
    modifier: Modifier = Modifier,
    onCapture: (ByteArray?) -> Unit,
    onFrameCaptured: (Any) -> Unit = {}
)
