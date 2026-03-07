package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.clases.FormServer
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.ServerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import kotlinx.coroutines.launch

//Metodo que permite poder ver los formularios disponibles subidos en el servidor
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerScreen(
    navController: NavController,
    answerViewModel: AnswerViewModel,
    serverViewModel: ServerViewModel
) {


    val formularios = serverViewModel.formularios
    val loading = serverViewModel.loading
    val error = serverViewModel.error
    val context = LocalContext.current

    var panelExpanded by remember { mutableStateOf(false) }

    var serverUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        if(serverViewModel.baseUrl != null){
            serverViewModel.resetPaginacion()
            serverViewModel.getForms()
        }

        if (serverViewModel.baseUrl != null && serverUrl.isBlank()) {
            serverUrl = serverViewModel.baseUrl!!
        }

    }

    Scaffold(

        containerColor = Color.Transparent,

        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                ),
                title = {
                    Text("Servidor", fontWeight = FontWeight.Bold)
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(
                color = Color(0xFF060434),
                thickness = 5.dp
            )

            Spacer(Modifier.height(12.dp))

            // CAMPO PARA INSERTAR LA URL SERVIDOR
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
                shape = RoundedCornerShape(14.dp)
            ) {

                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { panelExpanded = !panelExpanded }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Cloud,
                            contentDescription = null,
                            tint = Color.White
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "Servidor API",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            if (panelExpanded) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    AnimatedVisibility(panelExpanded) {

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {

                            OutlinedTextField(
                                value = serverUrl,
                                onValueChange = { serverUrl = it },
                                label = { Text("URL servidor", color = Color(0xFFF6F6F6)) },
                                placeholder = {
                                    Text("https://xxxxx.ngrok-free.dev/rest-api-proyectono1-compi1/api/v1/", color = Color(
                                        0xFFA7A6A9
                                    )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,

                                    focusedBorderColor = Color(0xFF6A2DA8),
                                    unfocusedBorderColor = Color.LightGray,

                                    cursorColor = Color(0xFFF7F7F8),

                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                ),
                            )

                            Spacer(Modifier.height(10.dp))

                            Button(
                                onClick = {

                                    serverViewModel.setBaseUrl(serverUrl)

                                    serverViewModel.testConnection(

                                        onSuccess = {

                                            Toast
                                                .makeText(context,"Conectado al servidor",Toast.LENGTH_SHORT)
                                                .show()

                                            serverViewModel.getForms()

                                        },

                                        onError = {

                                            Toast
                                                .makeText(context,"Servidor no válido",Toast.LENGTH_SHORT)
                                                .show()

                                            serverUrl = ""

                                        }
                                    )

                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Icon(Icons.Default.Cloud, contentDescription = null)

                                Spacer(Modifier.width(8.dp))

                                Text("Conectar al servidor")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            when {

                loading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = error!!,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                formularios.isEmpty() -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E1E1E)
                            )
                        ) {

                            Text(
                                "No hay formularios subidos en el servidor",
                                modifier = Modifier.padding(24.dp),
                                color = Color.White
                            )
                        }
                    }
                }
                else -> {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(formularios) { form ->

                            ServerFormCard(
                                form = form,
                                navController = navController,
                                answerViewModel = answerViewModel,
                                serverViewModel = serverViewModel
                            )
                        }

                        item {
                            Spacer(Modifier.height(30.dp))
                        }

                        item {
                            //Boton para paginar
                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = { serverViewModel.loadMoreForms() },
                                enabled = serverViewModel.hasMore,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Text(
                                    if(serverViewModel.hasMore)
                                        "Cargar más"
                                    else
                                        "No hay más formularios"
                                )

                            }

                            Spacer(Modifier.height(30.dp))
                        }
                    }



                }
            }
        }
    }
}

//Metodo que permite crear las cartas PARA PODER MOSTRAR LOS FORMULARIOS SUBIDOS EN EL SERVER
@Composable
fun ServerFormCard(
    form: FormServer,
    navController: NavController,
    answerViewModel: AnswerViewModel,
    serverViewModel: ServerViewModel
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val createFileLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("text/plain")
        ) { uri ->

            uri?.let {

                scope.launch {

                    val body = serverViewModel.downloadForm(form.id)

                    body?.let {

                        context.contentResolver.openOutputStream(uri)?.use { output ->

                            val input = body.byteStream()
                            input.copyTo(output)

                        }
                    }
                }
            }
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
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
                "Subido: ${form.fecha} ${form.hora}",
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

                        createFileLauncher.launch("${form.nombre}.txt")

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

                    Text("Descargar", color = Color.White)
                }


                Button(
                    modifier = Modifier.weight(1f),

                    onClick = {

                        scope.launch {

                            val codigo =
                                serverViewModel.getFormContent(form.id)

                            codigo?.let {

                                answerViewModel.setCodigoProcesado(it)

                                navController.navigate("answer")
                            }
                        }
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

                    Text("Contestar", color = Color.White)
                }
            }
        }
    }
}