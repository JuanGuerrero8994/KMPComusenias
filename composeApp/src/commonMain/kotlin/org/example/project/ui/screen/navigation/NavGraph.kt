package org.example.project.ui.screen.navigation

import org.example.project.ui.screen.auth.AuthScreen
import org.example.project.ui.screen.auth.SignUpScreen
import org.example.project.ui.screen.splash.SplashScreen
import org.example.project.ui.theme.EMPTY_STRING
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavScreen
import com.example.comuseniaskmm.android.ui.screen.home.HomeScreen


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


        //BOTTOM NAVIGATION BAR

        composable(
            route = BottomNavScreen.Home.route,
            arguments = listOf(navArgument("path") { type = NavType.StringType })
        ) {
            val path = it.arguments?.getString("path") ?: EMPTY_STRING
            HomeScreen(navController = navController, path = path)
        }




    }
}