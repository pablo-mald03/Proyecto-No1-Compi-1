package com.pablocompany.proyectono1_compi1.ui.screens.editor

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.data.repository.EditorViewModel
import com.pablocompany.proyectono1_compi1.data.repository.EditorViewModelFactory
import com.pablocompany.proyectono1_compi1.data.repository.FormFileRepository
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//------METODO PRINCIPAL QUE PERMITE MOSTRAR LA PANTALLA DEL EDITOR DE TEXTO-------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    navController: NavController,
    sharedFormViewModel: SharedFormViewModel
) {

    val leftDrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showErrorDrawer by remember { mutableStateOf(false) }

    var showColorPicker by remember { mutableStateOf(false) }

    /* ---------- Banner flotante ---------- */
    var hayErrores by remember { mutableStateOf(false) }

    LaunchedEffect(hayErrores) {
        if (hayErrores) {
            delay(3000)
            hayErrores = false
        }
    }

    /* --------------------------APARTADO DE VALIDACION DE VARIABLES Y DEMAS COSAS-------------------------------- */

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

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val repository = remember { FormFileRepository(context) }
    val viewModel: EditorViewModel = viewModel(
        factory = EditorViewModelFactory(repository)
    )

    val fileNameObserved by viewModel::fileName
    val isModified by viewModel::isModified

    val fileName = fileNameObserved ?: "Sin título"
    val title = if (isModified) "$fileName *" else fileName

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue != SheetValue.Expanded) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    val openLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.loadFile(it) }
    }

    val saveAsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        uri?.let { viewModel.saveAs(it) }
    }

    var showUnsavedDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    if (showUnsavedDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedDialog = false },
            title = { Text("Cambios sin guardar") },
            text = { Text("Tienes cambios sin guardar. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    showUnsavedDialog = false
                    pendingAction?.invoke()
                }) { Text("Continuar") }
            },
            dismissButton = {
                TextButton(onClick = { showUnsavedDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    val openFormLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        uri?.let { selectedUri ->

            var nombreArchivo = ""

            val cursor = context.contentResolver.query(
                selectedUri,
                null,
                null,
                null,
                null
            )

            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst() && nameIndex >= 0) {
                    nombreArchivo = it.getString(nameIndex)
                }
            }

            if (!nombreArchivo.lowercase().endsWith(".pkm")) {
                Toast.makeText(
                    context,
                    "Seleccione un archivo extension .pkm",
                    Toast.LENGTH_SHORT
                ).show()
                return@let
            }

            val contenido = context.contentResolver
                .openInputStream(selectedUri)
                ?.bufferedReader()
                ?.use { reader -> reader.readText() }
                ?: ""

            sharedFormViewModel.setCodigo(contenido)
            navController.navigate("form")
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                onAbrirCodigo = {
                    if (viewModel.isModified) {
                        pendingAction = {
                            openLauncher.launch(arrayOf("application/octet-stream"))
                        }
                        showUnsavedDialog = true
                    } else {
                        openLauncher.launch(arrayOf("application/octet-stream"))
                    }
                },
                onGuardarCodigo = {
                    if (viewModel.currentFileUri != null) {
                        viewModel.saveFile()
                    } else {
                        saveAsLauncher.launch("ArchivoEntrada")
                    }
                },
                onCerrarCodigo = {
                    if (viewModel.isModified) {
                        pendingAction = { viewModel.closeFile() }
                        showUnsavedDialog = true
                    } else {
                        viewModel.closeFile()
                    }
                },
                onAbrirFormulario = {
                    openFormLauncher.launch(arrayOf("application/octet-stream"))
                },
                onGuardarFormulario = {
                    val compilado = viewModel.compilarFormulario()

                    if (!compilado) {
                        hayErrores = true
                        showErrorDrawer = true
                    } else {
                        val codigoFinal = viewModel.codigoGenerado ?: ""
                        sharedFormViewModel.setCodigo(codigoFinal)
                        navController.navigate("form")
                    }
                },
                currentFileUri = viewModel.currentFileUri,
                isModified = viewModel.isModified,
                fileName = fileName
            )
        },
        drawerState = leftDrawerState
    ) {

        Box(modifier = Modifier.fillMaxSize()) {


            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 36.dp,
                sheetContainerColor = Color(0xFF1E1E1E),
                containerColor = Color.Transparent,
                sheetContent = {
                    ConsoleSection(
                        codeField = viewModel.codeField,
                        highlightedCode = viewModel.highlightedCode,
                        onCodeChange = { viewModel.updateCodeField(it) },
                        onFinalizarClick = {
                            //Instruccion de compilacion absoluta
                            val compilado = viewModel.compilarFormulario()

                            if (!compilado) {
                                hayErrores = true
                                showErrorDrawer = true
                            } else {
                                hayErrores = false
                                showErrorDrawer = false

                                val codigoFinal = viewModel.codigoGenerado
                                sharedFormViewModel.setCodigo(codigoFinal ?: "")
                                navController.navigate("form")
                            }
                        },
                        onReemplazarClick = {
                            val hayErroresFormales = viewModel.ejecutarAnalisisFormal()

                            if (hayErroresFormales) {
                                hayErrores = true
                                showErrorDrawer = true
                            } else {
                                hayErrores = false
                                showErrorDrawer = false
                                // Flujo normal tras compilado PENDIENTE (MODIFICAR UI)

                            }
                        },
                        onAgregarClick = {//Pendiente definir bloques de codigo

                        },
                        onColorClick = {
                            showColorPicker = true
                        }
                    )
                }
            ) {

                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        AnimatedVisibility(visible = !isExpanded) {
                            TopAppBar(
                                windowInsets = WindowInsets.statusBars,
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent,
                                    titleContentColor = Color.White,
                                    navigationIconContentColor = Color.White
                                ),
                                title = {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch { leftDrawerState.open() }
                                        }
                                    ) {
                                        Icon(Icons.Default.Menu, null)
                                    }
                                }
                            )
                        }
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
                            "Previsualización del Formulario",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(24.dp))

                        repeat(10) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF2C2C2C)
                                )
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Campo dinámico #$index",
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

            /* ---------------- Boton flotante de errores---------------- */

            FloatingActionButton(
                onClick = { showErrorDrawer = !showErrorDrawer },
                containerColor =
                    if (viewModel.listaErrores.isNotEmpty())
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

            /* ---------------- Fondo oscuro del reporte ---------------- */

            AnimatedVisibility(visible = showErrorDrawer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { showErrorDrawer = false }
                )
            }

            /* ---------------- DRAWER DERECHO ---------------- */

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
                        .width(360.dp)
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
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            IconButton(
                                onClick = { showErrorDrawer = false }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = Color.White
                                )
                            }
                        }

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.3f)
                        )

                        ErrorDrawerContent(errores = viewModel.listaErrores)
                    }
                }
            }

            /* ---------------- BANNER FLOTANTE ---------------- */
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

            /* ---------------- SELECTOR DE COLORES ---------------- */
            if (showColorPicker) {
                ColorPickerDialog(
                    onDismiss = { showColorPicker = false },
                    onColorSelected = { formattedColor ->
                        viewModel.insertTextAtCursor(formattedColor)
                    }
                )
            }
        }
    }
}
//Permite mostrar el contenido del drawer de los resportes. Es decir que muestra los errores y sus tablas
@Composable
fun ErrorDrawerContent(
    errores: List<ErrorAnalisis>
) {

    val horizontalScroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F0F0F),
                        Color(0xFF0A0A2C),
                        Color(0xFF331133),
                        Color(0xFF0C0C0C)
                    ),
                    start = Offset.Zero,
                    end = Offset(800f, 1200f)
                )
            )
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                text = "Reporte de Errores",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                stickyHeader {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(horizontalScroll)
                            .background(Color(0xFF3A0316))
                    ) {
                        CeldaHeader("Lexema")
                        CeldaHeader("Linea")
                        CeldaHeader("Columna")
                        CeldaHeader("Tipo")
                        CeldaHeader("Descripcion")
                    }
                }

                //val errores = viewModel
                itemsIndexed(errores) { index, error ->

                    val backgroundColor =
                        if (index % 2 == 0) Color(0xFF104457)
                        else Color(0xFF0A4633)

                    Row(
                        modifier = Modifier
                            .horizontalScroll(horizontalScroll)
                            .background(backgroundColor)
                    ) {
                        Celda(error.cadena ?: "")
                        Celda(error.linea.toString())
                        Celda(error.columna.toString())
                        Celda(error.tipo ?: "")
                        Celda(error.descripcion ?: "")
                    }
                }
            }
        }
    }
}

//Permite mostrar el banner flotante de errores (el mensaje flotante)
@Composable
fun ErrorBanner(
    mensaje: String,
    onClose: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Red
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mensaje,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
            }
        }
    }
}

//Metodo que permite formar los headers de las tablas (composable) es decir que es compartible
@Composable
fun CeldaHeader(texto: String) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFFFFFFF)
            )
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = Color(0xFFE3F2FD),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

//Metodo que permite formar las celdas de las tablas por fila
@Composable
fun Celda(texto: String) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFFFFFFF)
            )
            .padding(8.dp)
    ) {
        Text(
            text = texto,
            color = Color(0xFFCFD8DC),
            textAlign = TextAlign.Start,
            softWrap = true,
            maxLines = Int.MAX_VALUE
        )
    }
}

//Metodo que permite ejecutar las acciones de opciones
@Composable
fun DrawerContent(
    onAbrirCodigo: () -> Unit,
    onGuardarCodigo: () -> Unit,
    onCerrarCodigo: () -> Unit,
    onAbrirFormulario: () -> Unit,
    onGuardarFormulario: () -> Unit,
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
            "Opciones",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = fileName,
            color = if (currentFileUri != null) Color.Green else Color.Yellow,
            style = MaterialTheme.typography.bodySmall
        )

        if (isModified) {
            Text(
                "Cambios sin guardar",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        //Para codigo nativo
        Spacer(Modifier.height(24.dp))

        DrawerButton("Abrir código", onAbrirCodigo)
        DrawerButton("Guardar código", onGuardarCodigo)

        if (currentFileUri != null) {
            DrawerButton("Cerrar código", onCerrarCodigo)
        }

        Spacer(Modifier.height(16.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )

        Spacer(Modifier.height(16.dp))

        //Para para formularios
        DrawerButton("Abrir formulario", onAbrirFormulario)
        DrawerButton("Guardar formulario", onGuardarFormulario)
    }
}

//Metodo que permite dibujar los botones para la configuracion
@Composable
fun DrawerButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2C2C2C)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

//Metodo que permite despliegar la consola la seccion plegable de la consola
@Composable
fun ConsoleSection(
    codeField: TextFieldValue,
    highlightedCode: AnnotatedString,
    onCodeChange: (TextFieldValue) -> Unit,
    onFinalizarClick: () -> Unit,
    onReemplazarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onColorClick: () -> Unit,
) {

    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val isKeyboardVisible = imeBottom > 0

    val scrollState = rememberScrollState()

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Consola",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onColorClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF590613)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = "Color",
                        tint = Color.White
                    )

                    Spacer(Modifier.width(6.dp))

                    Text("Insertar Color", color = Color.White)
                }
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = codeField,
                onValueChange = onCodeChange,
                maxLines = Int.MAX_VALUE,
                visualTransformation = {
                    val safeHighlighted = if (codeField.text.isEmpty()) AnnotatedString("") else highlightedCode
                    TransformedText(
                        safeHighlighted,
                        OffsetMapping.Identity
                    )
                },
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF121212),
                    unfocusedContainerColor = Color(0xFF121212),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF9A031E)
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace
                ),
                placeholder = {
                    Text(
                        "Escribe tu código aquí...",
                        color = Color.Gray
                    )
                }
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = if (isKeyboardVisible) 62.dp else 0.dp
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ConsoleButton("Agregar", onClick = onAgregarClick)
                ConsoleButton("Reemplazar", onClick = onReemplazarClick)
                ConsoleButton("Finalizar", onClick = onFinalizarClick)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

//Consola para poder realizar el picker de colores
@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    onColorSelected: (String) -> Unit
) {
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var selectedFormat by remember { mutableStateOf("HEX") }

    var selectedPresetName by remember { mutableStateOf<String?>(null) }

    val controller = rememberColorPickerController()

    AlertDialog(
        containerColor = Color(0xFF020226),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {

                val result = selectedPresetName ?: when (selectedFormat) {
                    "HEX" -> selectedColor.toHex()
                    "RGB" -> selectedColor.toRgb()
                    "HSL" -> selectedColor.toHsl()
                    else -> selectedColor.toHex()
                }

                onColorSelected(result)
                onDismiss()
            }) {
                Text("Insertar", color = Color(0xFF33CC12))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFFFFFFFF))
            }
        },
        text = {
            Column {

                //Presets de la aplicacion propia
                val presets = listOf(
                    "RED" to Color(0xFFFF0000),
                    "GREEN" to Color(0xFF00FF00),
                    "BLUE" to Color(0xFF0000FF),
                    "PURPLE" to Color(0xFF9C27B0),
                    "SKY" to Color(0xFF03A9F4),
                    "YELLOW" to Color(0xFFFFEB3B),
                    "BLACK" to Color(0xFF000000),
                    "WHITE" to Color(0xFFFFFFFF)
                )

                Text(
                    text = "Presets",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(presets) { (name, colorValue) ->

                        Box(
                            modifier = Modifier
                                .background(colorValue, RoundedCornerShape(50))
                                .border(
                                    width = if (selectedColor == colorValue) 2.dp else 1.dp,
                                    color = if (selectedColor == colorValue)
                                        Color(0xFFBB86FC)
                                    else
                                        Color.DarkGray,
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable {
                                    selectedColor = colorValue
                                    selectedPresetName = name
                                    controller.selectByColor(colorValue, fromUser = true)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = name,
                                color = if (colorValue.luminance() > 0.5f)
                                    Color.Black
                                else
                                    Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                //Picker de colores
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    controller = controller,
                    onColorChanged = { envelope: ColorEnvelope ->
                        selectedColor = envelope.color
                        selectedPresetName = null
                    }
                )

                Spacer(Modifier.height(20.dp))

                // ===== Botones de formato =====
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("HEX", "RGB", "HSL").forEach { format ->
                        Button(
                            onClick = { selectedFormat = format },
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (selectedFormat == format)
                                        Color(0xFF6A1B9A)
                                    else
                                        Color(0xFF2A2A2A)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(format, color = Color.White)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                val previewText = when (selectedFormat) {
                    "HEX" -> selectedColor.toHex()
                    "RGB" -> selectedColor.toRgb()
                    "HSL" -> selectedColor.toHsl()
                    else -> selectedColor.toHex()
                }

                // ===== Preview visual del color =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(selectedColor, RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.height(16.dp))

                // ===== Preview estilo del codigo =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {

                        Text(
                            text = "Vista previa del formato: ",
                            color = Color(0xFF6A9955),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = previewText,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}

//Retorna formato HSL de colores
fun Color.toHsl(): String {
    val r = red
    val g = green
    val b = blue

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    var h = 0f
    val l = (max + min) / 2f
    val s = if (delta == 0f) 0f else delta / (1f - kotlin.math.abs(2f * l - 1f))

    if (delta != 0f) {
        h = when (max) {
            r -> ((g - b) / delta) % 6f
            g -> ((b - r) / delta) + 2f
            else -> ((r - g) / delta) + 4f
        }
        h *= 60f
        if (h < 0) h += 360f
    }

    return "<${h.toInt()}, ${(s * 100).toInt()}, ${(l * 100).toInt()}>"
}

//Retorna colores hexadecimales
fun Color.toHex(): String {
    return String.format(
        "#%02X%02X%02X",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

//Retorna colores rgb
fun Color.toRgb(): String {
    return "(${(red * 255).toInt()}, ${(green * 255).toInt()}, ${(blue * 255).toInt()})"
}

@Composable
fun ConsoleButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF5F0F40)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}
