package com.pablocompany.proyectono1_compi1.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.ServerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import com.pablocompany.proyectono1_compi1.ui.screens.editor.AnswerScreen
import com.pablocompany.proyectono1_compi1.ui.screens.editor.EditorScreen
import com.pablocompany.proyectono1_compi1.ui.screens.editor.FormScreen
import com.pablocompany.proyectono1_compi1.ui.screens.editor.ServerScreen

@Composable
fun MainScaffold() {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val sharedFormViewModel: SharedFormViewModel = viewModel()
    val answerViewModell: AnswerViewModel = viewModel()
    val serverViewModel: ServerViewModel = viewModel()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2E1A47),
                        Color(0xFF5F0F40),
                        Color(0xFF9A031E)
                    )
                )
            )
    ) {

        Scaffold(
            containerColor = Color.Transparent,

            bottomBar = {

                NavigationBar(
                    containerColor = Color(0xFF1E1E1E),
                    tonalElevation = 0.dp
                ) {

                    NavigationBarItem(
                        selected = currentRoute == "editor",
                        onClick = {
                            navController.navigate("editor") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = null
                            )
                        },
                        label = { Text("Editor") },
                        colors = navItemColors()
                    )

                    NavigationBarItem(
                        selected = currentRoute == "form",
                        onClick = {
                            navController.navigate("form") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Build,
                                contentDescription = null
                            )
                        },
                        label = { Text("Form") },
                        colors = navItemColors()
                    )

                    NavigationBarItem(
                        selected = currentRoute == "answer",
                        onClick = {
                            navController.navigate("answer") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.AdUnits,
                                contentDescription = null
                            )
                        },
                        label = { Text("Contestar") },
                        colors = navItemColors()
                    )

                    NavigationBarItem(
                        selected = currentRoute == "server",
                        onClick = {
                            navController.navigate("server") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null
                            )
                        },
                        label = { Text("Server") },
                        colors = navItemColors()
                    )
                }
            }

        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = "editor",
                modifier = Modifier.padding(padding)
            ) {

                //Metodo que sirve para invocar la pantalla del editor
                composable("editor") {
                    EditorScreen(
                        navController = navController,
                        sharedFormViewModel = sharedFormViewModel
                    )
                }

                //Metodo que permite invocar la pantalla del formulario
                composable("form") {
                    FormScreen(
                        navController = navController,
                        sharedFormViewModel = sharedFormViewModel,
                        answerViewModel = answerViewModell,
                        serverViewModel = serverViewModel
                    )
                }

                //Metodo que despliega la pantalla para poder contestar el formulario
                composable("answer") {
                    AnswerScreen(
                        navController = navController,
                        answerViewModel = answerViewModell
                    )
                }

                //Metodo que despliega la pantalla del servidor
                composable("server") {
                    ServerScreen(
                        navController = navController,
                        answerViewModel = answerViewModell,
                        serverViewModel = serverViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color(0xFF9A031E),
    selectedTextColor = Color(0xFF9A031E),
    unselectedIconColor = Color.White.copy(alpha = 0.7f),
    unselectedTextColor = Color.White.copy(alpha = 0.7f),
    indicatorColor = Color(0xFF5F0F40)
)

