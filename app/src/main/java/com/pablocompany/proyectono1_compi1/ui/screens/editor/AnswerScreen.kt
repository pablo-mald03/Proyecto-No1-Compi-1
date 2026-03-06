package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import com.pablocompany.proyectono1_compi1.domain.usecase.AnalizarFormularioUseCase
import kotlinx.coroutines.delay

//Vista que sirve para contestar los formularios (Independientemente)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(
    navController: NavController,
    sharedFormViewModel: SharedFormViewModel,
    answerViewModel: AnswerViewModel
) {

    val codigoProcesadoForm = sharedFormViewModel.codigoProcesado

    var showCloseDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    /* ===== ANALISIS ===== */

    val analizarFormulario = remember { AnalizarFormularioUseCase() }

    var showErrorDrawer by remember { mutableStateOf(false) }
    var hayErrores by remember { mutableStateOf(false) }

    val errores = answerViewModel.listaErrores

    LaunchedEffect(codigoProcesadoForm) {
        answerViewModel.setCodigoProcesado(codigoProcesadoForm)
    }

    LaunchedEffect(hayErrores) {
        if (hayErrores) {
            delay(3000)
            hayErrores = false
        }
    }

    val codigo by answerViewModel.codigoFormularioState

    LaunchedEffect(codigo) {

        if (codigo.isBlank()) {
            answerViewModel.limpiarResultado()
            return@LaunchedEffect
        }

        val resultado = analizarFormulario.ejecutar(codigo)

        answerViewModel.setResultadoAnalisis(resultado)

        if (resultado.errores.isNotEmpty()) {
            hayErrores = true
        }
    }

    /* ===== UI PRINCIPAL PARA PODER VER EL FORM Y PODERLO CONTESTAR ===== */

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            containerColor = Color.Transparent,

            topBar = {

                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    ),

                    title = {
                        Text(
                            "Contestar Formulario",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Spacer(Modifier.height(15.dp))

                HorizontalDivider(
                    color = Color(0xFF060434),
                    thickness = 5.dp
                )

                Spacer(Modifier.height(24.dp))

                if (codigo.isBlank()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E1E1E)
                            ),
                            border = BorderStroke(1.dp, Color(0xFF3A3A3A)),
                            shape = RoundedCornerShape(14.dp)
                        ) {

                            Text(
                                "No hay formulario seleccionado para contestar",
                                modifier = Modifier.padding(24.dp),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                } else {

                    /* FORMULARIO TEMPORAL */

                    repeat(5) { index ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),

                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2C2C2C)
                            )
                        ) {

                            Column(
                                Modifier.padding(16.dp)
                            ) {

                                Text(
                                    "Pregunta ${index + 1}",
                                    color = Color.White
                                )

                                Spacer(Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = "",
                                    onValueChange = {},
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {

                        Button(
                            onClick = { showDialog = true },

                            modifier = Modifier.weight(1f),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF04643C)
                            ),

                            shape = RoundedCornerShape(14.dp)

                        ) {

                            Text(
                                "Enviar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }


                        Button(
                            onClick = { showCloseDialog = true },

                            modifier = Modifier.weight(1f),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF09066E)
                            ),

                            shape = RoundedCornerShape(14.dp)

                        ) {

                            Text(
                                "Cerrar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(60.dp))
                }
            }
        }

        /* ===== BOTON ERRORES FLOTANTE ===== */

        FloatingActionButton(
            onClick = { showErrorDrawer = !showErrorDrawer },

            containerColor =
                if (errores.isNotEmpty())
                    Color(0xFFB00020)
                else
                    Color(0xFF04643C),

            contentColor = Color.White,

            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 35.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = "Ver errores")
        }

        /* ===== FONDO OSCURO QUE PERMITE ENCERRAR EL MENU DE ERRORES ===== */

        AnimatedVisibility(visible = showErrorDrawer) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showErrorDrawer = false }
            )
        }

        /* ===== PANEL ERRORES QUE PERMITE VER ERRORES POR SI ACASO FUE MANIPULADO EL FORM ===== */

        AnimatedVisibility(
            visible = showErrorDrawer,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {

            Surface(
                tonalElevation = 12.dp,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(
                        min = 320.dp,
                        max = 600.dp
                    )
            ) {

                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF0F0F0F),
                                        Color(0xFF3A051A),
                                        Color(0xFF021712),
                                        Color(0xFF461904)
                                    ),
                                    start = Offset.Zero,
                                    end = Offset(800f, 1200f)
                                )
                            )
                            .padding(12.dp),

                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "Errores",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        IconButton(
                            onClick = { showErrorDrawer = false }
                        ) {
                            Icon(Icons.Default.Close, null, tint = Color.White)
                        }
                    }

                    HorizontalDivider()

                    ErrorDrawerContent(errores = errores)
                }
            }
        }

        /* ===== BANNER DE ERRORES ===== */

        AnimatedVisibility(
            visible = hayErrores,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        ) {

            ErrorBanner(
                mensaje = "Se encontraron errores",
                onClose = { hayErrores = false },
                onClick = {
                    hayErrores = false
                    showErrorDrawer = true
                }
            )
        }
    }

    /* ===== DIALOG PARA CERRAR DEL FORMULARIO (SIEMPRE PREGUNTA SI CERRAR) ===== */

    if (showCloseDialog) {

        AlertDialog(

            onDismissRequest = { showCloseDialog = false },
            containerColor = Color(0xFF030821),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),

            title = {
                Text(
                    "Cerrar formulario",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    "¿Seguro que deseas cerrar el formulario?\n\nSe perderán todas las respuestas."
                )
            },

            confirmButton = {

                OutlinedButton(
                    onClick = {

                        answerViewModel.setCodigoProcesado("")
                        showCloseDialog = false

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7A0C0C)
                    )
                ) {
                    Text("Cerrar")
                }
            },

            dismissButton = {

                OutlinedButton(
                    onClick = { showCloseDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF34AB02)
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    /* ===== RESULTADO DEL FORMULARIO (PERMITE CALIFICAR LAS RESPUESTAS) ===== */

    if (showDialog) {

        AlertDialog(

            onDismissRequest = { showDialog = false },

            containerColor = Color(0xFF1E1E1E),

            title = {
                Text(
                    "Resultado",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    "Calificación: 85 / 100",
                    color = Color(0xFFCCCCCC)
                )
            },

            confirmButton = {

                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D47A1)
                    )
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}