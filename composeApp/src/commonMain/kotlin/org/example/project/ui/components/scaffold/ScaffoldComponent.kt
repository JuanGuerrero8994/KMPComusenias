package org.example.project.ui.components.scaffold


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kmpcomusenias.composeapp.generated.resources.Res
import kmpcomusenias.composeapp.generated.resources.background_login
import org.example.project.ui.components.scaffold.bottomNavBar.BottomNavigationBar
import org.jetbrains.compose.resources.painterResource


@Composable
fun ScaffoldComponent(
    navController: NavController,
    showTopBar: Boolean = true,
    showBottomBar: Boolean = true,
    onLogout: () -> Unit = {},
    floatingActionButton: @Composable (() -> Unit)? = null, // Nuevo parámetro para el FAB
    content: @Composable () -> Unit
) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBarComponent(onLogout = onLogout)
            }
        },

        bottomBar = {
           if (showBottomBar) {
               BottomNavigationBar(navController)

           }
        },
        floatingActionButton = floatingActionButton ?: {}, // Asignamos el parámetro FAB aquí
    ) { innerPadding ->

        // Colocamos el contenido del Scaffold
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(resource = Res.drawable.background_login),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().zIndex(0f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // El contenido específico de la pantalla se coloca aquí
                content()
            }
        }
    }
}
