package org.example.project.ui.navigation

sealed class Destinations(val route: String) {

    //SPLASH SCREEN
    data object SplashScreen : Destinations("splashScreen")

    //HOME SCREEN
    data object HomeScreen : Destinations("HomeScreen")

    //AUTH  SCREEN
    data object AuthScreen : Destinations("authScreen")

    //SIGN UP SCREEN
    data object SignUpScreen : Destinations("signUpScreen")

    //RESET PASSWORD SCREEN
    data object ResetPasswordScreen : Destinations("resetPasswordScreen")

    //CAMERA  PREVIEW SCREEN
    data object CameraPreviewScreen : Destinations("cameraPreviewScreen")

    //CAMERA  SCREEN
    data object CameraScreen : Destinations("cameraScreen")

}