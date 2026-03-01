package com.pablocompany.proyectono1_compi1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pablocompany.proyectono1_compi1.ui.screens.HomeScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        //Ruta que permite ir a home
        composable("home") {
            HomeScreen(navController)
        }

        //Ruta que permite ir al editor
        composable("editor") {
            //EditorScreen(navController, viewModel)
        }
    }
}