package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.FormViewModel

//Vista que sirve para contestar los formularios (Independientemente)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(
    navController: NavController,
    answerViewModel: AnswerViewModel
) {

    val codigo by answerViewModel.codigoFormularioState

    var showCloseDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    /* ===== ANALISIS ===== */

    var showErrorDrawer by remember { mutableStateOf(false) }
    var hayErrores by remember { mutableStateOf(false) }

    val errores = answerViewModel.listaErrores

    val interpretado = answerViewModel.codigoFormularioStateInterprete

    /*Permite ver de forma nativa las coordenadas o*/
    var useNativePosition by remember { mutableStateOf(false) }

    /*Viewmodel de respuestas*/
    val viewModel: FormViewModel = viewModel()

    LaunchedEffect(errores) {
        if (errores.isNotEmpty()) {
            hayErrores = true
        }
    }

    LaunchedEffect(codigo) {

        if (codigo.isBlank()) {
            answerViewModel.limpiarResultado()
            return@LaunchedEffect
        }

        answerViewModel.procesarCodigo(codigo)
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
                    title = { Text("Contestar Formulario", fontWeight = FontWeight.Bold) }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /*Switch para intercalar modos*/
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Diseño Nativo", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                if (useNativePosition) "Usando coordenadas .pkm" else "Ajustado a la pantalla",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Switch(
                            checked = useNativePosition,
                            onCheckedChange = { useNativePosition = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF34AB02),
                                checkedTrackColor = Color(0xFF34AB02).copy(alpha = 0.4f),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray
                            )
                        )
                    }
                }


                if (codigo.isBlank() || interpretado == null || interpretado.codigo.isEmpty()) {

                    Spacer(Modifier.height(100.dp))

                    Icon(
                        imageVector = Icons.Default.Airplay,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.White.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay formulario abierto",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Regresa al editor y compila tu código .pkm.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.3f),
                        textAlign = TextAlign.Center
                    )

                } else {

                    val scrollState = rememberScrollState()

                    val maxX = interpretado.codigo.maxOfOrNull {
                        (it.pointX?.toFloat() ?: 0f) + (it.width?.toFloat() ?: 0f)
                    } ?: 1000f

                    val maxY = interpretado.codigo.maxOfOrNull {
                        (it.pointY?.toFloat() ?: 0f) + (it.height?.toFloat() ?: 0f)
                    } ?: 1000f

                    /* --- CALCULOS PARA ADAPTAR A LA PANTALLA --- */
                    val configuration = LocalConfiguration.current
                    val screenHeight = configuration.screenHeightDp.toFloat()
                    val screenWidth = configuration.screenWidthDp.toFloat()
                    val scale = screenWidth / 800f


                    HorizontalDivider(
                        color = Color(0xFF060434),
                        thickness = 5.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (useNativePosition) {

                        val contentWidth = (maxX * scale).dp
                        val contentHeight = (maxY * scale).dp


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.TopCenter
                        )
                        {
                            Box(
                                modifier = Modifier
                                    .width(contentWidth)
                                    .height(contentHeight)
                                    .background(Color.White.copy(alpha = 0.02f))
                            ) {
                                interpretado.codigo.forEach { componente ->
                                    RenderComponent(
                                        component = componente,
                                        viewModel = viewModel,
                                        scale = scale,
                                        usePosition = true
                                    )
                                }
                            }
                        }
                    } else {
                        interpretado.codigo.forEach { componente ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                RenderComponent(
                                    component = componente,
                                    viewModel = viewModel,
                                    scale = scale,
                                    usePosition = false
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    showDialog = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF04643C
                                    )
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Enviar", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showCloseDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF09066E
                                    )
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(80.dp))
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

                            answerViewModel.limpiarResultado()
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

        /* ===== RESULTADO DEL FORMULARIO QUE PERMITE CALIFICAR LAS RESPUESTAS===== */

        if (showDialog) {
            val resultado = remember(showDialog) {
                viewModel.caluclarPuntaje(interpretado?.codigo ?: emptyList())
            }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color(0xFF1E1E1E),
                title = {
                    Text(
                        text = if (resultado.second > 0) "Resultado de Evaluacion" else "Formulario Enviado",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (resultado.second > 0) {
                            val porcentaje =
                                (resultado.first.toFloat() / resultado.second * 100).toInt()

                            Text(
                                text = "$porcentaje / 100",
                                style = MaterialTheme.typography.displayMedium,
                                color = if (porcentaje >= 61) Color(0xFF4CAF50) else Color(
                                    0xFFF44336
                                ),
                                fontWeight = FontWeight.Black
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Aciertos: ${resultado.first} de ${resultado.second}",
                                color = Color.LightGray
                            )
                        } else {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Tu respuesta ha sido procesada con exito.",
                                color = Color(0xFFCCCCCC),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false

                            //viewModel.clear()
                            // answerViewModel.limpiarResultado()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                    ) {
                        Text("Aceptar", color = Color.White)
                    }
                }
            )
        }
    }
}