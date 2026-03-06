package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    val codigoProcesadoForm = sharedFormViewModel.codigoProcesado

    print(codigoProcesadoForm)

    LaunchedEffect(codigoProcesadoForm) {
        answerViewModel.setCodigoProcesado(codigoProcesadoForm)
    }

    val codigo by answerViewModel.codigoFormularioState

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

            Text(
                "Formulario",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            if (codigo.isBlank()) {

                Text(
                    "No hay formulario seleccionado para contestar",
                    color = Color.White
                )

            } else {

                /* ---------------- FORMULARIO (QUEMADO) ---------------- */

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
                                "Pregunta $index",
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
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}
