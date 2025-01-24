package org.example.project.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

// Helper para rutas simples
fun NavGraphBuilder.addRoute(
    navController: NavHostController,
    route: String,
    content: @Composable (NavHostController) -> Unit
) {
    composable(route) {
        content(navController)
    }
}

// Helper para rutas de Bottom Navigation
fun NavGraphBuilder.addBottomNavRoute(
    navController: NavHostController,
    route: String,
    content: @Composable (NavHostController) -> Unit
) {
    composable(route) {
        content(navController)
    }
}


// Helper para rutas con argumentos
fun NavGraphBuilder.addRouteWithArgs(
    navController: NavHostController,
    route: String,
    arguments: List<NamedNavArgument>,
    content: @Composable (NavHostController, NavBackStackEntry) -> Unit
) {
    composable(route, arguments) { backStackEntry ->
        content(navController, backStackEntry)
    }
}