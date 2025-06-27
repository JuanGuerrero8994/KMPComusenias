package org.example.project.ui.screen.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import org.example.project.ui.components.scaffold.ScaffoldComponent
import org.example.project.data.core.Resource
import org.example.project.ui.base.HandleResourceState
import org.example.project.ui.components.customViews.LoadingDialog
import org.example.project.ui.screen.auth.AuthViewModel
import org.example.project.ui.screen.navigation.Destinations

import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel = koinViewModel()) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()

    // Observar el resultado del cierre de sesión
    val signOutResult by viewModel.signOutResult.collectAsState()
    val isLoading = viewModel.loading.collectAsState()

    val displayName = viewModel.currentUserState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.checkCurrentUser()
    }

    ScaffoldComponent(
        navController = navController,
        showTopBar = true,
        showBottomBar = true,
        onLogout = { viewModel.signOut() },
        floatingActionButton = null
    ) {
        Column(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) { keyboardController?.hide() }.focusRequester(focusRequester),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido! ${displayName.value}")

            // Mostrar el resultado del cierre de sesión
            HandleResourceState(
                resource = signOutResult,
                isLoading = isLoading,
                onSuccess = {   navController.navigate(Destinations.SplashScreen.route) {
                    popUpTo(0) { inclusive = true }// Limpia el back stack
                } },
            )

        }
    }
}

