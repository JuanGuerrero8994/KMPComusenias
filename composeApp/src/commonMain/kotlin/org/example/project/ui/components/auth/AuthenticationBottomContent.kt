package com.example.comuseniaskmm.android.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.example.project.ui.screen.navigation.Destinations
import org.example.project.ui.theme.SIZE14
import org.example.project.ui.theme.SIZE5
import org.example.project.ui.theme.blackColorApp
import org.example.project.ui.theme.primaryColorApp


@Composable
fun AuthenticationBottomContent(
    textOne: String,
    textTwo: String,
    navController: NavController
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = textOne,
            fontSize = SIZE14.sp,
            color = blackColorApp
        )
        Spacer(modifier = Modifier.width(SIZE5.dp))
        Text(
            modifier = Modifier.testTag("textclickfooter").clickable { navController.navigate(
                Destinations.SignUpScreen.route)},
            text = textTwo,
            color = primaryColorApp,
            fontSize = SIZE14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}