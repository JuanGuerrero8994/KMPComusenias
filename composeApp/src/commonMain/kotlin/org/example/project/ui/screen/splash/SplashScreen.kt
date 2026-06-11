package org.example.project.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import org.example.project.data.core.Resource
import org.example.project.domain.model.user.User
import org.example.project.ui.screen.auth.AuthViewModel
import org.example.project.ui.navigation.Destinations
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {

    val currentUser by viewModel.currentUserState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkCurrentUser()
    }

    LaunchedEffect(currentUser) {

        when (val state = currentUser) {

            is Resource.Loading -> Unit

            is Resource.Success -> {
                val user = state.data

                val destination = if (user != null) {
                    BottomNavScreen.Home.route
                } else {
                    Destinations.AuthScreen.route
                }

                navController.navigate(destination) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }

            is Resource.Error -> {
                navController.navigate(Destinations.AuthScreen.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }

            null -> Unit
        }
    }
}
