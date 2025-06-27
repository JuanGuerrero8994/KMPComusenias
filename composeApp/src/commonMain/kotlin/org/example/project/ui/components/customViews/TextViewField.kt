package org.example.project.ui.components.customViews

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.example.project.constants.TestTag
import org.example.project.ui.theme.SIZE14
import org.example.project.ui.theme.placeholderTextColor

@Composable
fun TextViewField(
    modifier: Modifier = Modifier,
    label: String,
    color: Color = placeholderTextColor,
    fontSize: Int = SIZE14,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlignment: TextAlign = TextAlign.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag(TestTag.TAG_TEXT_APP).align(Alignment.CenterHorizontally),
            color = color,
            text = label,
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            textAlign = textAlignment
        )
    }
}
