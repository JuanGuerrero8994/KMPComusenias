package org.example.project.ui.screen.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import org.example.project.ui.components.scaffold.ScaffoldComponent
import org.example.project.ui.base.HandleResourceState
import org.example.project.ui.viewModel.AuthViewModel
import org.example.project.ui.navigation.Destinations

import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {

    val signOutResult by viewModel.signOutResult.collectAsState()
    val isLoading = viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkCurrentUser()

        navController.navigate(Destinations.CameraPreviewScreen.route) {
            popUpTo(Destinations.HomeScreen.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    ScaffoldComponent(
        navController = navController,
        showTopBar = true,
        showBottomBar = true,
        onLogout = { viewModel.signOut() },
        floatingActionButton = null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            HandleResourceState(
                resource = signOutResult,
                isLoading = isLoading,
                onSuccess = {
                    navController.navigate(Destinations.SplashScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
