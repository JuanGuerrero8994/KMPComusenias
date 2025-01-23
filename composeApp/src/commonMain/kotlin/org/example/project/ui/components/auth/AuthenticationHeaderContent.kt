package org.example.project.ui.components.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kmpcomusenias.composeapp.generated.resources.Res
import kmpcomusenias.composeapp.generated.resources.icon_login_screen
import org.example.project.ui.theme.ICONAPP
import org.example.project.ui.theme.SIZE150
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthenticationHeaderContent(widthImage : Int = SIZE150, heightImage : Int = SIZE150) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderImage( widthImage = widthImage, heightImage = heightImage)
    }
}

@Composable
fun HeaderImage(widthImage: Int, heightImage: Int) {
    Image(
        painter = painterResource(resource = Res.drawable.icon_login_screen),
        contentDescription = ICONAPP,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .width(widthImage.dp)
            .height(heightImage.dp)
    )
}