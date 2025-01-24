package org.example.project.ui.screen.navigation

sealed class Destinations(val route: String) {

    //SPLASHSCREEN
    data object SplashScreen : Destinations("splashScreen")

    //AUTH  SCREEN
    data object AuthScreen : Destinations("authScreen")

    //SIGN UP SCREEN
    data object SignUpScreen : Destinations("signUpScreen")


}