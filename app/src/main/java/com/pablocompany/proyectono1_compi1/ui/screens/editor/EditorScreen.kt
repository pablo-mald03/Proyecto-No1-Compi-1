package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.pablocompany.proyectono1_compi1.data.repository.EditorViewModel
import com.pablocompany.proyectono1_compi1.data.repository.EditorViewModelFactory
import com.pablocompany.proyectono1_compi1.data.repository.FormFileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen() {

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

    /* ----------------------------------------------------------- */

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
                        saveAsLauncher.launch("archivo.form")
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
                        onCodeChange = { viewModel.updateCodeField(it) },
                        onGuardarClick = {
                            if (viewModel.currentFileUri != null) {
                                viewModel.saveFile()
                            } else {
                                saveAsLauncher.launch("archivo.form")
                            }
                        },
                        onEjecutarClick = {
                            hayErrores = true
                        },
                        onAgregarClick = {},
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
                containerColor = if (hayErrores) Color(0xFFB00020) else Color(0xFF04643C),
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

                        ErrorDrawerContent()
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
@Composable
fun ErrorDrawerContent() {

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

                items(2) { index ->

                    val backgroundColor =
                        if (index % 2 == 0) Color(0xFF104457)
                        else Color(0xFF0A4633)

                    Row(
                        modifier = Modifier
                            .horizontalScroll(horizontalScroll)
                            .background(backgroundColor)
                    ) {
                        Celda("id")
                        Celda("12")
                        Celda("5")
                        Celda("Lexico")
                        Celda("Identificador no reconocido en la expresión")
                    }
                }
            }
        }
    }
}

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

        Spacer(Modifier.height(24.dp))

        DrawerButton("Abrir código", onAbrirCodigo)
        DrawerButton("Guardar código", onGuardarCodigo)

        if (currentFileUri != null) {
            DrawerButton("Cerrar código", onCerrarCodigo)
        }
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
    onCodeChange: (TextFieldValue) -> Unit,
    onGuardarClick: () -> Unit,
    onEjecutarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onColorClick: () -> Unit
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
                horizontalArrangement = Arrangement.End
            ) {
                ConsoleButton("Color", onClick = onColorClick)
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = codeField,
                onValueChange = onCodeChange,
                maxLines = Int.MAX_VALUE,
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
                ConsoleButton("Ejecutar", onClick = onEjecutarClick)
                ConsoleButton("Guardar", onClick = onGuardarClick)
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

    val controller = rememberColorPickerController()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {

                val result = when (selectedFormat) {
                    "HEX" -> selectedColor.toHex()
                    "RGB" -> selectedColor.toRgb()
                    else -> selectedColor.toHex()
                }

                onColorSelected(result)
                onDismiss()
            }) {
                Text("Insertar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        text = {
            Column {

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    controller = controller,
                    onColorChanged = { envelope: ColorEnvelope ->
                        selectedColor = envelope.color
                    }
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { selectedFormat = "HEX" }) {
                        Text("HEX")
                    }
                    Button(onClick = { selectedFormat = "RGB" }) {
                        Text("RGB")
                    }
                }
            }
        }
    )
}
fun Color.toHex(): String {
    return String.format(
        "#%02X%02X%02X",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun Color.toRgb(): String {
    return "rgb(${(red * 255).toInt()}, ${(green * 255).toInt()}, ${(blue * 255).toInt()})"
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