package org.example.project.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.example.project.constants.TestTag
import org.example.project.ui.theme.FORGOT_PASSWORD
import org.example.project.ui.theme.SIZE14
import org.example.project.ui.theme.primaryColorApp

@Composable
fun ForgetMyPasswordComponent(onClickText: () -> Unit = {}) {
    Text(
        modifier = Modifier.fillMaxWidth()
            .testTag(TestTag.TAG_FORGET_MY_PASS)
            .clickable { onClickText() },
        text = FORGOT_PASSWORD,
        color = primaryColorApp,
        fontSize = SIZE14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End,
    )
}
