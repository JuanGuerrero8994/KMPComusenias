package org.example.project.ui.components.scaffold.bottomNavBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavScreen("homeScreen/{path}", "Home", Icons.Default.Home){ fun createRoute(path:String) = "homeScreen/${path}" }
    data object Profile : BottomNavScreen("profileScreen", "Profile", Icons.Default.Person)
    data object Settings : BottomNavScreen("settingsScreen", "Settings", Icons.Default.Settings)
}