package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pablocompany.proyectono1_compi1.data.repository.AnswerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.FormViewModel
import com.pablocompany.proyectono1_compi1.data.repository.ServerViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import com.pablocompany.proyectono1_compi1.domain.usecase.UploadFormUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    sharedFormViewModel: SharedFormViewModel,
    answerViewModel: AnswerViewModel,
    serverViewModel: ServerViewModel
) {
    //Variable que permite ir analizando el codigo de entrada

    val context = LocalContext.current

    val codigo = sharedFormViewModel.codigoCompilado
    val interpretado = sharedFormViewModel.codigoInterpretado

    //Obtener la lista de errores que se presentan en el analisis
    val errores = sharedFormViewModel.listaErrores

    /*Bloquea cuanto esta el parseo*/
    val isParsing = sharedFormViewModel.isParsing

    val isModified = sharedFormViewModel.isModified
    val currentUri = sharedFormViewModel.currentFileUri

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    /*VIEWMODEL DE LA INFORMACION DE LAS PREGUNTAS*/
    val viewModel: FormViewModel = viewModel()

    /*Booleano qu permite intercambiar entre vistas*/
    var modoEditor by remember { mutableStateOf(true) }


    /* ---------------- Permite Mostrar el mensaje (Notificacion) de errores ---------------- */

    var showErrorDrawer by remember { mutableStateOf(false) }
    var hayErrores by remember { mutableStateOf(false) }

    LaunchedEffect(hayErrores) {
        if (hayErrores) {
            delay(3000)
            hayErrores = false
        }
    }

    LaunchedEffect(interpretado) {
        viewModel.clear()
    }


    /* ---------------- Permite analizar al cargar--------- */
    LaunchedEffect(errores) {
        if (errores.isNotEmpty()) {
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

    val componentes = interpretado?.codigo ?: emptyList()

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

            sharedFormViewModel.loadFromFile(
                it,
                codigo ?: "",
                name
            )
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

                onAbrirFormulario = {
                    openFormLauncher.launch(
                        arrayOf(
                            "application/octet-stream",
                            "text/plain"
                        )
                    )
                },

                onGuardarFormulario = {
                    guardarFormulario()
                },

                onCerrarFormulario = {
                    sharedFormViewModel.limpiar()
                },

                currentFileUri = currentUri,
                isModified = isModified,
                fileName = sharedFormViewModel.fileName ?: "Sin nombre",
                sharedFormViewModel = sharedFormViewModel,
                serverViewModel = serverViewModel
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
                                text = sharedFormViewModel.codigoProcesado.ifBlank { "Sin codigo interpretado" },
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.fillMaxWidth(),
                                softWrap = true
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            //Boton que permite refrescar los cambios (por si se necesita)
                            Button(

                                onClick = {

                                    sharedFormViewModel.reanalizar()

                                    if (errores.isNotEmpty()) {
                                        hayErrores = true
                                    }
                                },
                                enabled = interpretado != null && !isParsing,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF04643C),
                                    contentColor = Color.White,

                                    disabledContainerColor = Color(0xFF96E181).copy(alpha = 0.3f),
                                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                                ),

                                shape = RoundedCornerShape(14.dp)

                            ) {

                                Icon(
                                    imageVector = Icons.Default.Autorenew,
                                    contentDescription = "Actualizar",
                                    tint = Color.White
                                )

                                Spacer(Modifier.width(6.dp))

                                Text(
                                    "Actualizar",
                                    color = Color.White
                                )
                            }

                            //Boton que permite navegar a contestar el formulario
                            Button(

                                onClick = {

                                    if (interpretado != null) {

                                        answerViewModel.setCodigoProcesado(sharedFormViewModel.codigoProcesado)

                                        navController.navigate("answer")

                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No hay formulario interpretado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                },
                                modifier = Modifier.weight(1f),
                                enabled = interpretado != null && !isParsing,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0D47A1),
                                    contentColor = Color.White,

                                    disabledContainerColor = Color(0xFF81A5E1).copy(alpha = 0.3f),
                                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                                ),

                                shape = RoundedCornerShape(14.dp)

                            ) {

                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Contestar",
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
            ) {innerPadding ->

                /* ---------------- Pantalla principal ---------------- */

                Scaffold(
                    modifier = Modifier.padding(innerPadding),
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
                                        Icon(
                                            Icons.Default.Menu, null,
                                            tint = Color.White
                                        )
                                    }
                                }
                            )
                        }
                    }

                ) { scaffoldPadding ->

                    /* ---------------- Área visual del formulario ---------------- */

                    val maxX = componentes.maxOfOrNull {
                        (it.pointX?.toFloat() ?: 0f) + (it.width?.toFloat() ?: 0f)
                    } ?: 1000f

                    val maxY = componentes.maxOfOrNull {
                        (it.pointY?.toFloat() ?: 0f) + (it.height?.toFloat() ?: 0f)
                    } ?: 1000f

                    val configuration = LocalConfiguration.current

                    val screenWidth = configuration.screenWidthDp.toFloat()
                    val screenHeight = configuration.screenHeightDp.toFloat()

                    val BASE_WIDTH = 800f
                    val BASE_HEIGHT = 1600f

                    val scaleX = screenWidth / BASE_WIDTH
                    val scaleY = screenHeight / BASE_HEIGHT

                    val scale = minOf(scaleX, scaleY)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        "Vista de Diseño",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        if (modoEditor) "Interpretacion .pkm" else "Ajuste automático",
                                        color = Color.White.copy(alpha = 0.6f),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Switch(
                                    checked = modoEditor,
                                    onCheckedChange = { modoEditor = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF0D47A1),
                                        checkedTrackColor = Color(0xFF0D47A1).copy(alpha = 0.4f)
                                    )
                                )
                            }
                        }

                        /* ---------------- Área visual del formulario ---------------- */

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (componentes.isEmpty()) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Airplay,
                                        contentDescription = null,
                                        modifier = Modifier.size(80.dp),
                                        tint = Color.White.copy(alpha = 0.2f)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Esperando codigo fuente...",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.Light
                                    )

                                    Text(
                                        text = "Compila o abre un archivo .pkm para visualizar el formulario",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.3f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                if (modoEditor) {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                            .horizontalScroll(rememberScrollState()),
                                        contentAlignment = Alignment.TopStart
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width((maxX * scale).dp)
                                                .height((maxY * scale).dp)
                                                .background(Color.White.copy(alpha = 0.02f))
                                        ) {
                                            componentes.forEach { componente ->
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

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        componentes.forEach { componente ->
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
                                    }
                                }
                            }
                        }

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


//Funcion que evita espacios (por cualquier cosa) (ESTA ER NO INFLUYE EN LO ABSOLUTO EN EL PARSER NI EL LEXER CABE ACLARAR)
fun sanitizeFileName(name: String): String {
    return name
        .trim()
        .replace("\\s+".toRegex(), "_")
        .replace("[^A-Za-z0-9_]".toRegex(), "")
}

//Metodo que permite desplegar el contenido del menu de opciones
@Composable
fun DrawerFormContent(
    onAbrirFormulario: () -> Unit,
    onGuardarFormulario: () -> Unit,
    onCerrarFormulario: () -> Unit,

    currentFileUri: Uri?,
    isModified: Boolean,
    fileName: String,
    sharedFormViewModel: SharedFormViewModel,
    serverViewModel: ServerViewModel
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uploadUseCase = remember { UploadFormUseCase(context) }

    var showConfirmUpload by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var serverName by remember { mutableStateOf("") }

    var showUploadResult by remember { mutableStateOf(false) }
    var uploadSuccess by remember { mutableStateOf(false) }

    val formContent = sharedFormViewModel.codigoProcesado

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

        /* ---------- SEPARADOR PARA LA OPCION DE SUBIR AL SERVIDOR ---------- */

        Spacer(Modifier.height(36.dp))

        HorizontalDivider(color = Color.DarkGray)

        Spacer(Modifier.height(24.dp))

        DrawerButtonServer(
            text = "Subir formulario al servidor",
            icon = Icons.Default.CloudUpload
        ) {
            showConfirmUpload = true
        }
    }

    /* ---------------- CONFIRMACION ---------------- */
    var nombreAutor by remember { mutableStateOf("") }

    if (showConfirmUpload) {

        AlertDialog(

            onDismissRequest = { showConfirmUpload = false },
            containerColor = Color(0xFF030821),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),

            title = {
                Text("Subir formulario")
            },

            text = {
                Column {

                    Text("¿Deseas subir tu formulario al servidor?")

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombreAutor,
                        onValueChange = { nombreAutor = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Autor (opcional)")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            focusedBorderColor = Color(0xFFD0BCFF),
                            unfocusedBorderColor = Color.LightGray,

                            cursorColor = Color(0xFFD0BCFF),

                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        )
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showConfirmUpload = false

                        if (formContent.isBlank()) {

                            Toast.makeText(
                                context,
                                "No hay código compilado para subir",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@TextButton
                        }

                        if (sharedFormViewModel.listaErrores.isNotEmpty()) {

                            Toast.makeText(
                                context,
                                "El formulario contiene errores",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@TextButton
                        }


                        if (currentFileUri != null) {

                            val sanitizedFileName = sanitizeFileName(fileName)

                            scope.launch {
                                val autorFinal = nombreAutor.ifBlank { "anonimo" }

                                val success = serverViewModel.uploadForm(
                                    fileUri = currentFileUri,
                                    fileName = sanitizedFileName,
                                    autor = autorFinal
                                )

                                uploadSuccess = success
                                showUploadResult = true
                            }

                        } else {
                            showNameDialog = true
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFCCB8F8)
                    )

                ) {
                    Text("Subir")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = { showConfirmUpload = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFC4773)
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    /* ---------------- DIALOGO NOMBRE PARA SUBIR AL SERVIDOR ---------------- */

    if (showNameDialog) {

        AlertDialog(

            onDismissRequest = { showNameDialog = false },
            containerColor = Color(0xFF030821),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),

            title = {
                Text("Nombre del formulario")
            },

            text = {


                Column {

                    Text("Ingresa el nombre con el que se guardara en el servidor")

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = serverName,
                        onValueChange = { serverName = it },
                        placeholder = { Text("Nombre del formulario") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            focusedBorderColor = Color(0xFFD0BCFF),
                            unfocusedBorderColor = Color.LightGray,

                            cursorColor = Color(0xFFD0BCFF),

                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombreAutor,
                        onValueChange = { nombreAutor = it },
                        placeholder = { Text("Autor (opcional)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            focusedBorderColor = Color(0xFFD0BCFF),
                            unfocusedBorderColor = Color.LightGray,

                            cursorColor = Color(0xFFD0BCFF),

                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                    )
                }

            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val sanitizedName = sanitizeFileName(serverName)
                        val autorFinal = nombreAutor.ifBlank { "anonimo" }

                        scope.launch {

                            val success = serverViewModel.uploadFormContent(
                                content = formContent,
                                fileName = sanitizedName,
                                autor = autorFinal
                            )

                            uploadSuccess = success
                            showUploadResult = true
                            showNameDialog = false
                        }

                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFCCB8F8)
                    )

                ) {
                    Text("Subir")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = { showNameDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFC4773)
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    /* ---------------- DIALOGO PARA CONFIRMAR SI FUE EXITOSO O NO ---------------- */
    if (showUploadResult) {

        LaunchedEffect(showUploadResult) {
            kotlinx.coroutines.delay(3000)
            showUploadResult = false
        }

        AlertDialog(

            onDismissRequest = { showUploadResult = false },
            containerColor = Color(0xFF030821),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),

            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = if (uploadSuccess)
                            Icons.Default.CheckCircle
                        else
                            Icons.Default.Error,
                        contentDescription = null,
                        tint = if (uploadSuccess) Color(0xFF4CAF50) else Color(0xFFE53935)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        if (uploadSuccess)
                            "Subida exitosa"
                        else
                            "Error al subir"
                    )
                }
            },

            text = {

                Text(
                    if (uploadSuccess)
                        "Tu formulario se ha subido correctamente al servidor"
                    else
                        "Ocurrio un error al subir tu formulario"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = { showUploadResult = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFDA0964)
                    )
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

//Boton especial para subir al servidor
@Composable
fun DrawerButtonServer(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        shape = RoundedCornerShape(12.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2C2C2C)
        )
    ) {

        if (icon != null) {

            Icon(
                icon,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(Modifier.width(8.dp))
        }

        Text(
            text,
            color = Color.White
        )
    }
}

