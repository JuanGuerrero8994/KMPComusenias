package org.example.project.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import org.example.project.ui.screen.home.HomeScreen
import com.example.comuseniaskmm.android.ui.screen.profile.ProfileScreen

import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import org.example.project.ui.screen.auth.AuthScreen
import org.example.project.ui.screen.auth.AuthViewModel
import org.example.project.ui.screen.auth.SignUpScreen
import org.example.project.ui.screen.settings.SettingsScreen
import org.example.project.ui.screen.splash.SplashScreen
import org.example.project.ui.theme.EMPTY_STRING
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun NavGraph(navController: NavHostController) {

    val authViewModel:AuthViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = Destinations.SplashScreen.route) {

        // Rutas simples
        addRoute(navController, Destinations.SplashScreen.route) { SplashScreen(it,authViewModel) }
        addRoute(navController, Destinations.AuthScreen.route) { AuthScreen(it,authViewModel) }
        addRoute(navController, Destinations.SignUpScreen.route) { SignUpScreen(it) }

        /*------------------BOTTOM NAVIGATION BAR-----------------------*/
        addBottomNavRoute(navController,BottomNavScreen.Home.route){ HomeScreen(it,authViewModel) }
        addBottomNavRoute(navController, BottomNavScreen.Profile.route) { ProfileScreen(it,authViewModel) }
        addBottomNavRoute(navController, BottomNavScreen.Settings.route) { SettingsScreen(it,authViewModel) }

    }
}
