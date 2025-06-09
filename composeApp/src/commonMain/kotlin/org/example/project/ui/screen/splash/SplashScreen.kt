package org.example.project.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import org.example.project.data.core.Resource
import org.example.project.domain.model.auth.User
import org.example.project.ui.screen.auth.AuthViewModel
import org.example.project.ui.screen.navigation.Destinations
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(navController: NavController, viewModel: AuthViewModel = koinViewModel()) {
    val currentUser by viewModel.currentUserState.collectAsState()

    LaunchedEffect(currentUser) {
        when (currentUser) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                val user = (currentUser as Resource.Success<User?>).data
                if (user != null) {
                    // Usuario autenticado, redirigir a HomeScreen
                    navController.navigate(BottomNavScreen.Home.route) {
                        popUpTo(BottomNavScreen.Home.route) { inclusive = true }
                    }
                } else {
                    // No hay usuario autenticado, redirigir a AuthScreen
                    navController.navigate(Destinations.AuthScreen.route) {
                        popUpTo(Destinations.AuthScreen.route) { inclusive = true }
                    }
                }
            }

            is Resource.Error -> {}

        }
    }


}
