package org.example.project.ui.components.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.project.ui.theme.AVATAR
import org.example.project.ui.theme.SIZE3
import org.example.project.ui.theme.SIZE5
import org.example.project.ui.theme.SIZE64
import org.example.project.ui.theme.SIZE8

@Composable
fun ImageWhithBorder(
    image: String,
    borderColor: State<Color>,
    border: Int = SIZE3,
) {
    Box(
        modifier = Modifier
            .size(SIZE64.dp)
            .border(
                width = border.dp,
                color = borderColor.value,
                shape = RoundedCornerShape(SIZE8.dp)
            )
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(SIZE5.dp)
                .clip(shape = RoundedCornerShape(SIZE8.dp)),
            model = image,
            contentScale = Crop,
            contentDescription = AVATAR,
        )
    }
}