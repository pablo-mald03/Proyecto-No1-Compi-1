package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.clases.FormServer
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel

//Metodo que permite poder ver los formularios disponibles subidos en el servidor
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerScreen(
    navController: NavController,
    answerViewModel: AnswerViewModel
) {

    //Datos temporales quemados (luego vendrán del GET)
    val formularios = listOf(
        FormServer("Examen Matemática", "Pablo Maldonado", "05/03/2026", "22:31"),
        FormServer("Evaluación Física", "Ana López", "04/03/2026", "18:20"),
        FormServer("Quiz Programación", "Carlos Ruiz", "03/03/2026", "15:05")
    )

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
                        "Servidor",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(Modifier.height(10.dp))

                HorizontalDivider(
                    color = Color(0xFF060434),
                    thickness = 5.dp
                )

                Spacer(Modifier.height(10.dp))
            }

            items(formularios) { form ->

                ServerFormCard(
                    form = form,
                    navController = navController,
                    answerViewModel = answerViewModel
                )
            }

            item {
                Spacer(Modifier.height(30.dp))
            }
        }
    }
}

//Metodo que permite crear las cartas PARA PODER MOSTRAR LOS FORMULARIOS SUBIDOS EN EL SERVER
@Composable
fun ServerFormCard(
    form: FormServer,
    navController: NavController,
    answerViewModel: AnswerViewModel
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),

        border = BorderStroke(1.dp, Color(0xFF3A3A3A)),

        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                form.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Creador: ${form.creador}",
                color = Color(0xFFCCCCCC),
                fontSize = 13.sp
            )

            Text(
                "Subido: ${form.fecha}  ${form.hora}",
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp,
                    Alignment.CenterHorizontally
                )
            ) {

                Button(

                    modifier = Modifier.weight(1f),

                    onClick = {

                        //PENDIENTE INTEGRAR EL REQUEST AL SERVIDOR
                        navController.navigate("answer")

                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF04643C)
                    ),

                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        "Descargar",
                        color = Color.White
                    )
                }


                Button(

                    modifier = Modifier.weight(1f),

                    onClick = {

                        //PENDIENTE INTEGRAR EL REQUEST PARA TRAER EL TEXTO
                        // val codigo = requestServidor()

                        val codigo = "requestServidor HOLA MUNDO"
                        answerViewModel.setCodigoProcesado(codigo)

                        navController.navigate("answer")
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D47A1)
                    ),

                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        "Contestar",
                        color = Color.White
                    )
                }
            }
        }
    }
}