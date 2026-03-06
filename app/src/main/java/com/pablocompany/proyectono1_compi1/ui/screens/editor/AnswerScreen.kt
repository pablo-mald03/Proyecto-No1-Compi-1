package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel

//Vista que sirve para contestar los formularios (Independientemente)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(
    navController: NavController,
    sharedFormViewModel: SharedFormViewModel,
    answerViewModel: AnswerViewModel
) {

    //Permite obtener el codigo procesado del formulario
    val codigoProcesadoForm = sharedFormViewModel.codigoProcesado

    //Permite determinar si mostrar un dialog para cerrar el formulario
    var showCloseDialog by remember { mutableStateOf(false) }

    LaunchedEffect(codigoProcesadoForm) {
        answerViewModel.setCodigoProcesado(codigoProcesadoForm)
    }

    val codigo by answerViewModel.codigoFormularioState

    var showDialog by remember { mutableStateOf(false) }

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

            /* ---------- ESPACIO BONITO DE SEPARACION ---------- */

            Spacer(Modifier.height(15.dp))

            HorizontalDivider(
                color = Color(0xFF060434),
                thickness = 5.dp
            )

            Spacer(Modifier.height(24.dp))


            /* ---------- TEXTO QUE SE MUETRA SI NO HAY FORMULARIO SI NO HAY FORMULARIO ---------- */

            if (codigo.isBlank()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
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

                /* ---------- FORMULARIO QUEMADO ---------- */

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

                /* ---------- BOTONES PARA CERRAR O CONTESTAR EL FORMULARIO---------- */
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

        //Permite mostrar el dialogo de confirmacion para cerrar
        if (showCloseDialog) {

            AlertDialog(

                onDismissRequest = { showCloseDialog = false },
                containerColor = Color(0xFF030821),
                titleContentColor = Color.White,
                textContentColor = Color(0xFFCCCCCC),

                title = {
                    Text(
                        "Cerrar formulario",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },

                text = {
                    Text(
                        "¿Seguro que deseas cerrar el formulario?\n\nSe perderán todas las respuestas.",
                        color = Color(0xFFCCCCCC)
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

    }

    /* ---------- DIALOG DE CALIFICACION (PENDIENTE)---------- */

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
