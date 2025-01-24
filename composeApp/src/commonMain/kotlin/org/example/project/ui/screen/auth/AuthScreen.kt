package org.example.project.ui.screen.auth

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comuseniaskmm.android.ui.components.auth.AuthenticationBottomContent
import org.example.project.data.core.Resource
import org.example.project.data.model.auth.RequestUser
import org.example.project.domain.model.auth.User
import org.example.project.ui.components.auth.AuthButton
import org.example.project.ui.components.auth.AuthenticationHeaderContent
import org.example.project.ui.components.auth.ForgetMyPasswordComponent
import org.example.project.ui.components.auth.OutlinedTextFieldComponent
import org.example.project.ui.components.custom_views.LoadingDialog
import org.example.project.ui.components.scaffold.ScaffoldComponent
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import org.example.project.ui.screen.navigation.Destinations
import org.example.project.ui.theme.EMAIL_TEXT
import org.example.project.ui.theme.HAVENT_ACCOUNT
import org.example.project.ui.theme.LOGIN
import org.example.project.ui.theme.PASSWORD
import org.example.project.ui.theme.REGISTER
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val signInResult by viewModel.signInResult.collectAsState()


    ScaffoldComponent(
        navController,
        showTopBar = false,
        showBottomBar = false,
        floatingActionButton = {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
                .pointerInput(Unit) { detectTapGestures { keyboardController?.hide() } },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthenticationHeaderContent()

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextFieldComponent(
                value = email,
                onValueChange = { email = it },
                label = EMAIL_TEXT,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextFieldComponent(
                label = PASSWORD,
                value = password,
                onValueChange = { password = it },
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            ForgetMyPasswordComponent(onClickText = {})
            Spacer(modifier = Modifier.height(16.dp))

            AuthButton(text = LOGIN, onClick = {
                isLoading = true
                val requestUser = RequestUser(email, password)
                viewModel.signIn(requestUser)
            })

            Spacer(modifier = Modifier.height(8.dp))
            AuthenticationBottomContent(textOne = HAVENT_ACCOUNT, textTwo = REGISTER, navController)

            ShowResults(signInResult, isLoading, navController)

        }
    }
}

@Composable
private fun ShowResults(
    signInResult: Resource<User>,
    isLoading: Boolean,
    navController: NavController
) {
    when (signInResult) {
        is Resource.Loading -> LoadingDialog(isLoading)
        is Resource.Success -> LaunchedEffect(Unit) {
            navController.navigate(BottomNavScreen.Home.route) {
                popUpTo(Destinations.AuthScreen.route) { inclusive = true }
            }
        }

        is Resource.Error -> Text("${signInResult.exception.message}", color = Color.Red)
    }
}


