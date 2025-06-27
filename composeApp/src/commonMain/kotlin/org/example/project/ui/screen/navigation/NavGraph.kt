package org.example.project.ui.screen.navigation

import org.example.project.ui.screen.auth.AuthScreen
import org.example.project.ui.screen.auth.SignUpScreen
import org.example.project.ui.screen.splash.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import org.example.project.ui.screen.home.HomeScreen
import org.example.project.ui.screen.profile.ProfileScreen
import org.example.project.ui.screen.auth.AuthViewModel
import org.example.project.ui.screen.auth.ResetPasswordScreen
import org.example.project.ui.screen.settings.SettingsScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Destinations.SplashScreen.route) {
        // Rutas simples
        addRoute(navController, Destinations.SplashScreen.route) { SplashScreen(it) }
        addRoute(navController, Destinations.AuthScreen.route) { AuthScreen(it) }
        addRoute(navController, Destinations.SignUpScreen.route) { SignUpScreen(it) }
        addRoute(navController, Destinations.ResetPasswordScreen.route) { ResetPasswordScreen(it) }

        /*------------------BOTTOM NAVIGATION BAR-----------------------*/
        addBottomNavRoute(navController,BottomNavScreen.Home.route){ HomeScreen(it) }
        addBottomNavRoute(navController, BottomNavScreen.Profile.route) { ProfileScreen(it) }
        addBottomNavRoute(navController, BottomNavScreen.Settings.route) { SettingsScreen(it) }

    }
}
