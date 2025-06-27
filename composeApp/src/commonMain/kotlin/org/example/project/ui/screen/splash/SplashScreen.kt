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
import org.example.project.ui.screen.navigation.Destinations
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(navController: NavController, viewModel: AuthViewModel = koinViewModel()) {
    val currentUser by viewModel.currentUserState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkCurrentUser()
    }

    LaunchedEffect(currentUser) {
        when (currentUser) {
            is Resource.Loading -> {} // Mostrá una animación si querés
            is Resource.Success -> {
                val user = (currentUser as Resource.Success<User?>).data
                if (user != null) {
                    navController.navigate(BottomNavScreen.Home.route) {
                        popUpTo(0) { inclusive = true } // limpia todo el backstack
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate(Destinations.AuthScreen.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            is Resource.Error -> {
                // Podés loguear o mostrar error si querés
                navController.navigate(Destinations.AuthScreen.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}
