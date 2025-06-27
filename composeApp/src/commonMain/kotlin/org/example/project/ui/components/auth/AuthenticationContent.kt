package org.example.project.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.ui.theme.SIZE20
import org.example.project.ui.theme.SIZE250
import org.example.project.ui.theme.SIZE30
import org.example.project.ui.theme.SIZE60

@Composable
fun AuthenticationContent(
    content: @Composable () -> Unit = {},
    footer: @Composable () -> Unit = {},
    sizeImage : Int = SIZE250
) {

    Column ( modifier = Modifier.fillMaxSize().padding(top = SIZE60.dp)
    ){
        AuthenticationHeaderContent(sizeImage, sizeImage)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = SIZE30.dp, end = SIZE30.dp, top = SIZE30.dp, bottom = SIZE20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            content()
            footer()
        }
    }

}
