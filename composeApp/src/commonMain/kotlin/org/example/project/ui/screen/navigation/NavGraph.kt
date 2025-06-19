package org.example.project.ui.screen.navigation

import org.example.project.ui.screen.auth.AuthScreen
import org.example.project.ui.screen.auth.SignUpScreen
import org.example.project.ui.screen.splash.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import org.example.project.ui.screen.home.HomeScreen
import com.example.comuseniaskmm.android.ui.screen.profile.ProfileScreen
import org.example.project.ui.screen.settings.SettingsScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destinations.SplashScreen.route) {
        composable(route= Destinations.SplashScreen.route){
            SplashScreen(navController)
        }

        composable(route = Destinations.AuthScreen.route) {
            AuthScreen(navController)
        }

        composable(route = Destinations.SignUpScreen.route) {
            SignUpScreen(navController)
        }

        // Rutas simples
        addRoute(navController, Destinations.SplashScreen.route) { SplashScreen(it) }
        addRoute(navController, Destinations.AuthScreen.route) { AuthScreen(it) }
        addRoute(navController, Destinations.SignUpScreen.route) { SignUpScreen(it) }

        /*------------------BOTTOM NAVIGATION BAR-----------------------*/
        addBottomNavRoute(navController,BottomNavScreen.Home.route){ HomeScreen(it) }
        addBottomNavRoute(navController, BottomNavScreen.Profile.route) { ProfileScreen(it) }
        addBottomNavRoute(navController, BottomNavScreen.Settings.route) { SettingsScreen(it) }

    }
}
