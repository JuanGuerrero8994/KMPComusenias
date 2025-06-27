package org.example.project.ui.screen.auth

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import org.example.project.ui.components.auth.AuthenticationFooterContent
import org.example.project.domain.model.user.User
import org.example.project.ui.base.HandleResourceState
import org.example.project.ui.components.auth.AuthenticationHeaderContent
import org.example.project.ui.components.auth.ButtonApp
import org.example.project.ui.components.auth.ForgetMyPasswordComponent
import org.example.project.ui.components.auth.OutlinedTextFieldComponent
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

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel = koinViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signInResult by viewModel.signInResult.collectAsState()
    val isLoading = viewModel.loading.collectAsState()
    val errorEmail = viewModel.emailError.collectAsState()
    val errorPassword = viewModel.passwordError.collectAsState()

    ScaffoldComponent(
        navController,
        showTopBar = false,
        showBottomBar = false,
        floatingActionButton = {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState)
                .pointerInput(Unit) { detectTapGestures { keyboardController?.hide() } },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            AuthenticationHeaderContent()

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextFieldComponent(
                value = email,
                onValueChange = { email = it },
                label = EMAIL_TEXT,
                keyboardType = KeyboardType.Email,
                errorText = errorEmail.value
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextFieldComponent(
                label = PASSWORD,
                value = password,
                onValueChange = { password = it },
                keyboardType = KeyboardType.Password,
                isPassword = true,
                errorText = errorPassword.value
            )
            Spacer(modifier = Modifier.height(16.dp))
            ForgetMyPasswordComponent(onClickText = { navController.navigate(Destinations.ResetPasswordScreen.route) {} })
            Spacer(modifier = Modifier.height(16.dp))

            ButtonApp(titleButton = LOGIN, onClickButton = {
                val user = User(email = email, password = password)
                viewModel.signIn(user)
            }, enabledButton = true)

            Spacer(modifier = Modifier.height(8.dp))
            AuthenticationFooterContent(
                textOne = HAVENT_ACCOUNT,
                textTwo = REGISTER,
                onClickText = { navController.navigate(Destinations.SignUpScreen.route) })

        }

    }
    HandleResourceState(resource = signInResult, isLoading = isLoading, onSuccess = {
        navController.navigate(BottomNavScreen.Home.route) {
            popUpTo(Destinations.AuthScreen.route) {
                inclusive = true
            }
        }
    }, onError = { Text("${it.message}", color = Color.Red) })
}

