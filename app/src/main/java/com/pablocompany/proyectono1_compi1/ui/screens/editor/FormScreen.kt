package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import com.pablocompany.proyectono1_compi1.domain.usecase.AnalizarFormularioUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    sharedFormViewModel: SharedFormViewModel
) {

    //Variable que permite ir analizando el codigo de entrada
    val analizarFormulario = remember { AnalizarFormularioUseCase() }

    val context = LocalContext.current

    val codigo = sharedFormViewModel.codigoCompilado

    val isModified = sharedFormViewModel.isModified
    val currentUri = sharedFormViewModel.currentFileUri

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    /* ---------------- Permite Mostrar el mensaje (Notificacion) de errores ---------------- */

    var showErrorDrawer by remember { mutableStateOf(false) }
    var hayErrores by remember { mutableStateOf(false) }

    LaunchedEffect(hayErrores) {
        if (hayErrores) {
            delay(3000)
            hayErrores = false
        }
    }

    //Obtener la lista de errores que se presentan en el analisis
    val errores = sharedFormViewModel.listaErrores

    /* ---------------- Permite analizar al cargar--------- */
    LaunchedEffect(codigo) {

        if (codigo.isNullOrBlank()) {

            sharedFormViewModel.limpiarResultado()
            return@LaunchedEffect
        }

        val resultado = analizarFormulario.ejecutar(codigo)

        sharedFormViewModel.setResultadoAnalisis(resultado)

        if (resultado.errores.isNotEmpty()) {
            hayErrores = true
        }
    }

    /* ---------------- Permite analizar al cargar--------- */

    /* ---------------- BottomSheet consola ---------------- */

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val isExpanded by remember {
        derivedStateOf { sheetState.currentValue == SheetValue.Expanded }
    }

    val scrollState = rememberScrollState()
    val consoleScroll = rememberScrollState()

    /* ---------------- Guardado de formularios ---------------- */

    //Funcion de guardado
    fun getFileName(context: Context, uri: Uri): String {
        var name = "Sin nombre"

        val cursor = context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            if (it.moveToFirst() && nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }

        return name
    }

    val saveAsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->

        uri?.let {

            context.contentResolver
                .openOutputStream(it)
                ?.bufferedWriter()
                ?.use { writer ->
                    writer.write(codigo)
                }

            val name = getFileName(context, it)

            sharedFormViewModel.loadFromFile(it, sharedFormViewModel.codigoCompilado ?: "",name)
        }
    }

    //Laucher que permite abrir un archivo
    val openFormLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        uri?.let {

            val input = context.contentResolver.openInputStream(it)
            val contenido = input?.bufferedReader()?.use { reader ->
                reader.readText()
            } ?: ""

            val name = getFileName(context, it)

            sharedFormViewModel.loadFromFile(it, contenido, name)
        }
    }


    fun guardarFormulario() {

        if (currentUri != null) {

            context.contentResolver
                .openOutputStream(currentUri, "wt")
                ?.bufferedWriter()
                ?.use { writer ->
                    writer.write(codigo ?: "")
                }

            sharedFormViewModel.markSaved()

        } else {
            saveAsLauncher.launch("Formulario.pkm")
        }
    }

    /* ---------------- Drawer ---------------- */

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            DrawerFormContent(

                onAbrirFormulario = { openFormLauncher.launch(arrayOf("application/octet-stream","text/plain"))},

                onGuardarFormulario = {
                    guardarFormulario()
                },

                onCerrarFormulario = {
                    sharedFormViewModel.limpiar()
                },

                currentFileUri = currentUri,
                isModified = isModified,
                fileName = sharedFormViewModel.fileName ?: "Sin nombre"
            )
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            /* ---------------- Consola inferior ---------------- */

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 36.dp,
                sheetContainerColor = Color(0xFF1E1E1E),
                containerColor = Color.Transparent,

                sheetContent = {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {

                        Text(
                            "Codigo Compilado",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .background(Color(0xFF121212))
                                .padding(12.dp)
                                .verticalScroll(consoleScroll)
                        ) {

                            Text(
                                text = if (sharedFormViewModel.codigoProcesado.isNotEmpty()){ sharedFormViewModel.codigoProcesado }
                                else{"Sin código compilado" },
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.fillMaxWidth(),
                                softWrap = true
                            )
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            ) {

                /* ---------------- Pantalla principal ---------------- */

                Scaffold(
                    containerColor = Color.Transparent,

                    topBar = {

                        AnimatedVisibility(visible = !isExpanded) {

                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent,
                                    titleContentColor = Color.White
                                ),

                                title = {
                                    Text(
                                        "Formulario",
                                        fontWeight = FontWeight.Bold
                                    )
                                },

                                navigationIcon = {

                                    IconButton(
                                        onClick = {
                                            scope.launch { drawerState.open() }
                                        }
                                    ) {
                                        Icon(Icons.Default.Menu, null,
                                            tint = Color.White)
                                    }
                                }
                            )
                        }
                    }

                ) { padding ->

                    /* ---------------- Área visual del formulario ---------------- */

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {

                        Text(
                            "Vista del formulario",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(24.dp))

                        /* ----- PENDIENTE RECIBIR CODIGO DE BACKEND (QUEMADO) ----- */

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
                                        "Campo $index",
                                        color = Color.White
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = {},
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(120.dp))
                    }
                }
            }

            /* ---------------- BOTON DE ERRORES ---------------- */

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

            //=====Vista del cuadro de errores======
            AnimatedVisibility(visible = showErrorDrawer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { showErrorDrawer = false }
                )
            }

            //=====Vista del apartado de errores======
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

            //=====Vista de notificacion flotante======
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
    }
}

@Composable
fun DrawerFormContent(

    onAbrirFormulario: () -> Unit,
    onGuardarFormulario: () -> Unit,
    onCerrarFormulario: () -> Unit,

    currentFileUri: Uri?,
    isModified: Boolean,
    fileName: String,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A).copy(alpha = 0.97f))
            .padding(24.dp)
    ) {

        Text(
            "Formulario",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = fileName,
            color = if (currentFileUri != null) Color.Green else Color.Yellow
        )

        if (isModified) {
            Text(
                "Cambios sin guardar",
                color = Color.Red
            )
        }

        Spacer(Modifier.height(24.dp))

        DrawerButton("Abrir formulario", onAbrirFormulario)

        DrawerButton("Guardar formulario", onGuardarFormulario)

        if (currentFileUri != null) {
            DrawerButton("Cerrar formulario", onCerrarFormulario)
        }
    }
}