package org.example.project.ui.screen.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.github.aakira.napier.Napier
import org.example.project.ui.base.HandleResourceState
import org.example.project.ui.components.auth.AuthenticationContent
import org.example.project.ui.components.auth.AuthenticationFooterContent
import org.example.project.ui.components.auth.ButtonApp
import org.example.project.ui.components.auth.OutlinedTextFieldComponent
import org.example.project.ui.components.auth.PasswordResetDialog
import org.example.project.ui.components.customViews.TextViewField
import org.example.project.ui.components.scaffold.ScaffoldComponent
import org.example.project.ui.screen.navigation.Destinations
import org.example.project.ui.theme.BUTTON_RESET_PASSWORD
import org.example.project.ui.theme.DO_YOU_ALREADY_HAVE_AN_ACCOUNT
import org.example.project.ui.theme.EMAIL_TEXT
import org.example.project.ui.theme.ENTER
import org.example.project.ui.theme.SIZE12
import org.example.project.ui.theme.SIZE90
import org.example.project.ui.theme.TEXT_RESET_PASSWORD
import org.example.project.ui.theme.primaryColorApp
import org.example.project.ui.viewModel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel =      koinViewModel()
) {
    ScaffoldComponent(
        navController,
        showTopBar = false,
        showBottomBar = false,
        floatingActionButton = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AuthenticationContent(
                content = {
                    ResetPasswordForm(
                        viewModel = viewModel,
                        navController = navController
                    )
                },
                footer = {
                    AuthenticationFooterContent(
                        textOne = DO_YOU_ALREADY_HAVE_AN_ACCOUNT,
                        textTwo = ENTER,
                        onClickText = {
                            navController.navigate(Destinations.AuthScreen.route) {
                                launchSingleTop = true
                                popUpTo(0)
                            }
                        }
                    )
                },
                sizeImage = SIZE90,
            )
        }

    }
}

@Composable
fun ResetPasswordForm(
    navController: NavController,
    viewModel: AuthViewModel,
) {

    var email by remember { mutableStateOf("") }
    val isLoading = viewModel.loading.collectAsState()
    val status = viewModel.resetPasswordState.collectAsState()
    val errorEmail = viewModel.emailError.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.padding(5.dp))

        OutlinedTextFieldComponent(
            value = email,
            onValueChange = { email = it },
            label = EMAIL_TEXT,
            keyboardType = KeyboardType.Email,
            errorText = errorEmail.value
        )

        Spacer(modifier = Modifier.padding(5.dp))

        TextViewField(
            modifier = Modifier,
            label = TEXT_RESET_PASSWORD,
            color = primaryColorApp,
            fontSize = SIZE12,
            textAlignment = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(20.dp))

        ButtonApp(
            titleButton = BUTTON_RESET_PASSWORD,
            onClickButton = {
                viewModel.resetPassword(email)
            }, enabledButton = true
        )

    }
    HandleResourceState(
        resource = status.value,
        isLoading = isLoading,
        onSuccess = {
            PasswordResetDialog(
                onDismiss = {},
                onButton = {
                    navController.navigate(route = Destinations.AuthScreen.route) {
                        launchSingleTop = true
                        popUpTo(0)
                    }
                }
            )
        },
        onError = {
            Napier.e("ResetPassword - Error: ${it.message}")
        }
    )
}
