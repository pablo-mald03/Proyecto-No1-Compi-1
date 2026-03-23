package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.CleaningServices
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
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
import com.pablocompany.proyectono1_compi1.data.repository.FormViewModel
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    var estaCompilando by remember { mutableStateOf(false) }

    //Control del teclado
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    //Atributos de guardado (Codigo compilado)
    var showMetadataDialog by remember { mutableStateOf(false) }
    var nombreAutor by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

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


    val context = LocalContext.current

    val repository = remember { FormFileRepository(context) }
    val viewModel: EditorViewModel = viewModel(factory = EditorViewModelFactory(repository))

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

    //=======Variables para poder ver si hay cambios sin guardar=====
    var showSaveFormDialog by remember { mutableStateOf(false) }
    var codigoPendiente by remember { mutableStateOf<String?>(null) }

    //Variable para mostrar el picker de colores
    var showTemplateDialog by remember { mutableStateOf(false) }

    /*VIEWMODEL DE LA INFORMACION DE LAS PREGUNTAS*/
    val viewModelPreguntas: FormViewModel = viewModel()


    //====Permite guardar el archivo con el codigo compilado al darle a guardar

    val saveFormLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->

        uri?.let {

            val codigo = codigoPendiente ?: return@rememberLauncherForActivityResult

            context.contentResolver
                .openOutputStream(it, "wt")
                ?.bufferedWriter()
                ?.use { writer ->
                    writer.write(codigo)
                }

            var nombreArchivo = ""

            val cursor = context.contentResolver.query(
                it,
                null,
                null,
                null,
                null
            )

            cursor?.use { c ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (c.moveToFirst() && nameIndex >= 0) {
                    nombreArchivo = c.getString(nameIndex)
                }
            }


            //Registrar archivo en el SharedViewModel
            sharedFormViewModel.loadFromFile(it, codigo, nombreArchivo)
            sharedFormViewModel.marcarDesdeEditor()
            navController.navigate("form")
        }
    }

    //Permite sobreescribir el formulario
    fun sobrescribirFormulario(codigo: String) {

        val uri = sharedFormViewModel.currentFileUri ?: return

        context.contentResolver
            .openOutputStream(uri, "wt")
            ?.bufferedWriter()
            ?.use { writer ->
                writer.write(codigo)
            }

        sharedFormViewModel.updateCodigoEditor(codigo)
        sharedFormViewModel.markSaved()
    }

    //====Permite guardar el archivo con el codigo compilado al darle a guardar

    //Permite guardar el codigo compilado
    if (showSaveFormDialog) {

        AlertDialog(
            onDismissRequest = { showSaveFormDialog = false },
            containerColor = Color(0xFF180321),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),
            title = { Text("Guardar formulario") },

            text = {
                Text("¿Deseas guardar el formulario antes de abrir la vista?")
            },

            confirmButton = {
                TextButton(

                    onClick = {
                        showSaveFormDialog = false
                        showMetadataDialog = true
                    }

                ) {
                    Text("Guardar")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {

                        showSaveFormDialog = false

                        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(Date())

                        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault())
                            .format(Date())

                        val header = """
###
    Author: android-app
    Fecha: $fechaActual
    Hora: $horaActual
    Descripcion: ...

""".trimIndent() + "\n"

                        codigoPendiente?.let {

                            val codigoFinal = header + it

                            scope.launch {

                                viewModel.interpretarCodigoCompilado(codigoFinal)

                                sharedFormViewModel.loadTemporary(codigoFinal)
                                navController.navigate("form")
                            }
                        }
                    }
                ) {
                    Text("No guardar")
                }
            }
        )
    }

    /*Metodo para ponerle el autor*/
    if (showMetadataDialog) {

        AlertDialog(
            onDismissRequest = { showMetadataDialog = false },
            containerColor = Color(0xFF180321),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),

            title = {
                Text("Información del formulario")
            },

            text = {
                Column {

                    OutlinedTextField(
                        value = nombreAutor,
                        onValueChange = { nombreAutor = it },
                        label = { Text("Nombre del autor") },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFBB86FC),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFFBB86FC),
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color(0xFFBB86FC)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFBB86FC),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFFBB86FC),
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color(0xFFBB86FC)
                        )
                    )
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {

                        showMetadataDialog = false

                        val nombreFinal =
                            if (nombreAutor.isBlank()) "android-app" else nombreAutor

                        val descripcionFinal =
                            if (descripcion.isBlank()) "..." else descripcion

                        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(Date())

                        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault())
                            .format(Date())

                        val header = """
###
    Author: $nombreFinal
    Fecha: $fechaActual
    Hora: $horaActual
    Descripcion: "$descripcionFinal"
    
""".trimIndent() + "\n"

                        val codigoFinal = header + (codigoPendiente ?: "")

                        scope.launch {

                            viewModel.interpretarCodigoCompilado(codigoFinal)

                            if (sharedFormViewModel.currentFileUri != null) {
                                sobrescribirFormulario(codigoFinal)
                                navController.navigate("form")
                            } else {
                                codigoPendiente = codigoFinal
                                saveFormLauncher.launch("Formulario.pkm")
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = { showMetadataDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


    //=======Variables para poder ver si hay cambios sin guardar=====


    if (showUnsavedDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedDialog = false },
            containerColor = Color(0xFF180321),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),
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

            sharedFormViewModel.loadFromFile(selectedUri, contenido, nombreArchivo)
            navController.navigate("form")
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                onAbrirCodigo = {
                    if (viewModel.isModified) {
                        pendingAction = {
                            openLauncher.launch(arrayOf("text/plain", "application/octet-stream"))
                        }
                        showUnsavedDialog = true
                    } else {
                        openLauncher.launch(arrayOf("text/plain", "application/octet-stream"))
                        sharedFormViewModel.desmarcarDesdeEditor()
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
                        sharedFormViewModel.desmarcarDesdeEditor()
                    }
                },
                onAbrirFormulario = {
                    openFormLauncher.launch(arrayOf("application/octet-stream"))
                },
                onGuardarFormulario = {

                    //Condicion que permite verificar antes de que se guarde el formulario (caso en el que se abro un formulario)
                    if (
                        sharedFormViewModel.codigoCompilado != null &&
                        !sharedFormViewModel.generadoDesdeEditor &&
                        sharedFormViewModel.currentFileUri?.toString() != viewModel.currentFileUri?.toString()
                    ) {

                        Toast.makeText(
                            context,
                            "Hay un formulario abierto. Ciérrelo antes de reemplazarlo.",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@DrawerContent
                    }

                    if (estaCompilando) {
                        return@DrawerContent
                    }
                    estaCompilando = true


                    viewModel.compilarFormulario { exito ->

                        estaCompilando = false

                        if (!exito) {

                            //Cierra el teclado si hay errores
                            keyboardController?.hide()
                            focusManager.clearFocus()

                            hayErrores = true
                        } else {
                            hayErrores = false
                            showErrorDrawer = false

                            val codigoFinal = viewModel.codigoGenerado ?: ""

                            codigoPendiente = codigoFinal
                            showSaveFormDialog = true
                        }
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

                            if (viewModel.codeField.text.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "El codigo fuente esta vacío",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@ConsoleSection
                            }

                            //Condicion que permite verificar antes de que se guarde el formulario (caso en el que se abro un formulario)
                            if (
                                sharedFormViewModel.codigoCompilado != null &&
                                !sharedFormViewModel.generadoDesdeEditor &&
                                sharedFormViewModel.currentFileUri?.toString() != viewModel.currentFileUri?.toString()
                            ) {

                                Toast.makeText(
                                    context,
                                    "Hay un formulario abierto. Ciérrelo antes de reemplazarlo.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@ConsoleSection
                            }

                            if (estaCompilando) {
                                return@ConsoleSection
                            }
                            estaCompilando = true

                            //Instruccion de compilacion absoluta
                            viewModel.compilarFormulario { exito ->

                                estaCompilando = false

                                if (!exito) {

                                    //Cierra el teclado si hay errores
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    hayErrores = true
                                    return@compilarFormulario
                                }


                                val codigoFinal = viewModel.codigoGenerado ?: ""

                                if (codigoFinal.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "El compilado generado esta vacio",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@compilarFormulario
                                }


                                hayErrores = false
                                showErrorDrawer = false



                                codigoPendiente = codigoFinal
                                showSaveFormDialog = true

                            }
                        },
                        onReemplazarClick = {

                            if (
                                sharedFormViewModel.codigoCompilado != null &&
                                !sharedFormViewModel.generadoDesdeEditor &&
                                sharedFormViewModel.currentFileUri?.toString() != viewModel.currentFileUri?.toString()
                            ) {

                                Toast.makeText(
                                    context,
                                    "Hay un formulario abierto diferente. Ciérrelo antes de actualizarlo.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@ConsoleSection
                            }

                            if (estaCompilando) return@ConsoleSection
                            estaCompilando = true

                            viewModel.compilarFormulario { exito ->

                                if (!exito) {
                                    estaCompilando = false
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    hayErrores = true
                                    showErrorDrawer = true

                                    return@compilarFormulario
                                }

                                val cuerpoCodigo = viewModel.codigoGenerado ?: ""

                                if (viewModel.codigoGenerado?.isBlank() == true) {
                                    estaCompilando = false
                                    Toast.makeText(
                                        context,
                                        "El compilado generado esta vacio",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@compilarFormulario
                                }

                                val nombreFinal =
                                    if (nombreAutor.isBlank()) "android-app" else nombreAutor

                                val descripcionFinal =
                                    if (descripcion.isBlank()) "..." else descripcion

                                val fechaActual =
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                        .format(Date())

                                val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    .format(Date())

                                val header = """
###
    Author: $nombreFinal
    Fecha: $fechaActual
    Hora: $horaActual
    Descripcion: "$descripcionFinal"
    
""".trimIndent() + "\n"


                                val codigoFinal = header + cuerpoCodigo

                                scope.launch {

                                    viewModel.interpretarCodigoCompilado(codigoFinal)

                                    estaCompilando = false
                                    hayErrores = false
                                    showErrorDrawer = false

                                    Toast.makeText(
                                        context,
                                        "Vista actualizada",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }
                        },
                        onAgregarClick = {
                            showTemplateDialog = true
                        },
                        onColorClick = {
                            showColorPicker = true
                        },
                        onLimpiarClick = {
                            viewModel.limpiarCodigo()
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

                    val estadoInterpretado = viewModel.codigoInterpretado
                    val componentesInterpretados = estadoInterpretado?.codigo ?: emptyList()

                    /*---INTEGRACION DE LA VISTA REAL DEL FORMULARIO----*/

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "Previsualizacion del Formulario",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(24.dp))

                        if (componentesInterpretados.isEmpty()) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(top = 100.dp)
                                    .alpha(0.4f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Airplay,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Sin componentes interpretados",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                        } else {

                            val maxX = componentesInterpretados.maxOfOrNull {
                                (it.pointX?.toFloat() ?: 0f) + (it.width?.toFloat() ?: 0f)
                            } ?: 1000f

                            val maxY = componentesInterpretados.maxOfOrNull {
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

                            componentesInterpretados.forEach { componente ->


                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    RenderComponent(
                                        component = componente,
                                        viewModel = viewModelPreguntas,
                                        scale = scale,
                                        usePosition = false
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(80.dp))
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

            /*------INSERTADOR DE PLANTILLAS-------*/
            if (showTemplateDialog) {
                TemplatePickerDialog(
                    onDismiss = { showTemplateDialog = false },
                    onTemplateSelected = { template ->

                        val current = viewModel.codeField.text
                        val cursorPosition = viewModel.codeField.selection.start
                        val safeCursor = cursorPosition.coerceIn(0, current.length)

                        val templateToInsert = "\n$template"

                        val nuevoTexto =
                            current.substring(0, safeCursor) +
                                    templateToInsert +
                                    current.substring(safeCursor)

                        val nuevaPosicion = safeCursor + templateToInsert.length

                        viewModel.updateCodeField(
                            TextFieldValue(
                                text = nuevoTexto,
                                selection = TextRange(nuevaPosicion)
                            )
                        )
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
    onLimpiarClick: () -> Unit
) {

    //Apartado de confirmacion de limpieza
    var showClearDialog by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val isKeyboardVisible = imeBottom > 0

    var estaCompilando by remember { mutableStateOf(false) }

    val editorScroll = rememberScrollState()

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Consola",
                fontWeight = FontWeight.Bold,
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

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = { showClearDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF057373)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = "Limpiar",
                        tint = Color.White
                    )

                    Spacer(Modifier.width(6.dp))

                    Text("Limpiar", color = Color.White)
                }
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = codeField,
                onValueChange = onCodeChange,
                maxLines = Int.MAX_VALUE,
                visualTransformation = {
                    val actualContent = if (highlightedCode.text.length == codeField.text.length) {
                        highlightedCode
                    } else {
                        AnnotatedString(codeField.text)
                    }

                    TransformedText(actualContent, OffsetMapping.Identity)
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

    if (showClearDialog) {

        AlertDialog(

            onDismissRequest = { showClearDialog = false },
            containerColor = Color(0xFF012F09),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC),

            title = {
                Text("Limpiar consola")
            },

            text = {
                Text("¿Seguro que deseas borrar todo el código?")
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showClearDialog = false
                        onLimpiarClick()
                    }
                ) {
                    Text("Sí")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showClearDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
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


/*Metodo composable que permite manejar las plantillas de codigo de la app*/
@Composable
fun TemplatePickerDialog(
    onDismiss: () -> Unit,
    onTemplateSelected: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF020226),
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color.White)
            }
        },
        text = {

            Column {

                Text(
                    text = "Insertar plantilla",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // ===== FUNCIONES SPECIAL =====
                    TemplateDropdownButton(
                        title = "COMPONENTES",
                        options = listOf(

                            "TEXTO" to {
                                onTemplateSelected(
                                    """
TEXT [
    width: 1, ${'$'}opcional 
    height: 1, ${'$'}opcional
    content: "contenido"
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "OPEN QUESTION" to {
                                onTemplateSelected(
                                    """
OPEN_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional
    label: "texto"
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "DROP QUESTION" to {
                                onTemplateSelected(
                                    """
DROP_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional
    label: "texto",

    options: {"primera", "segunda"},

    correct: 0
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "SELECT QUESTION" to {
                                onTemplateSelected(
                                    """
SELECT_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional

    options: {"primera", "segunda"},

    correct: 0
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "MULTIPLE QUESTION" to {
                                onTemplateSelected(
                                    """
MULTIPLE_QUESTION [
    width: 1, ${'$'}opcional
    height: 1, ${'$'}opcional

    options: {"primera", "segunda"},

    correct: {0, 1}
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }

                        )
                    )

                    // ===== LAYOUTS =====
                    TemplateDropdownButton(
                        title = "LAYOUTS",
                        options = listOf(

                            "SECTION" to {
                                onTemplateSelected(
                                    """
SECTION [
    width: 1,    ${'$'}Opcional
    height: 1,  ${'$'}Opcional
    
    pointX: 0,
    pointY: 0,
    
    orientation: VERTICAL,  ${'$'}Opcional  
    
    elements: {
        ${'$'}Elementos
    }
    
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "TABLE" to {
                                onTemplateSelected(
                                    """
TABLE [
    width: 1, ${'$'}Opcional
    height: 1, ${'$'}Opcional
    
    pointX: 1,
    pointY: 1,
    
    orientation: VERTICAL,
    
    elements: {
        [
            {
                ${'$'}Elemento
            }
        ]
    }
    
]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== EMOJIS =====
                    TemplateDropdownButton(
                        title = "EMOJIS",
                        options = listOf(

                            "SMILE" to {
                                onTemplateSelected(
                                    """
 @[:smile:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },

                            "SAD" to {
                                onTemplateSelected(
                                    """
 @[:sad:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },
                            "SERIOUS" to {
                                onTemplateSelected(
                                    """
 @[:serious:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },

                            "HEART" to {
                                onTemplateSelected(
                                    """
 @[:heart:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },
                            "STAR" to {
                                onTemplateSelected(
                                    """
 @[:star:]
                                """.trimIndent()
                                )
                                onDismiss()
                            },
                            "CAT" to {
                                onTemplateSelected(
                                    """
 @[:cat:]
                                """.trimIndent()
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== CONFIGURACIONES =====
                    TemplateDropdownButton(
                        title = "CONFIGURACIONES",
                        options = listOf(

                            "STYLES" to {
                                onTemplateSelected(
                                    """
    styles [
        "color": #000000,
        "background color": #000000,
        "font family": MONO,
        "text size": 1
    ]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "COLOR" to {
                                onTemplateSelected(
                                    """
        "color": #000000
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "BACKGROUND COLOR" to {
                                onTemplateSelected(
                                    """
        "background color": #000000
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "FONT FAMILY" to {
                                onTemplateSelected(
                                    """
        "font family": MONO
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "TEXT SIZE" to {
                                onTemplateSelected(
                                    """
        "text size": 1
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "BORDER" to {
                                onTemplateSelected(
                                    """
        "border": (1, DOTTED, #000000)
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== CONDICIONALES =====
                    TemplateDropdownButton(
                        title = "CONDICIONALES",
                        options = listOf(

                            "IF" to {
                                onTemplateSelected(
                                    """
IF (condicion) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "IF - ELSE" to {
                                onTemplateSelected(
                                    """
IF (condicion) {
    ${'$'}Contenido
} ELSE {
    
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "ELSE" to {
                                onTemplateSelected(
                                    """
ELSE {
    ${'$'}Contenido 
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "ELSE IF" to {
                                onTemplateSelected(
                                    """
ELSE IF (condicion){
    ${'$'}Contenido 
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== CICLOS =====
                    TemplateDropdownButton(
                        title = "CICLOS",
                        options = listOf(

                            "FOR" to {
                                onTemplateSelected(
                                    """
FOR (i = 0; i < 10; i = i + 1) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "WHILE" to {
                                onTemplateSelected(
                                    """
WHILE (condicion) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "DO-WHILE" to {
                                onTemplateSelected(
                                    """
DO {
    ${'$'}Contenido
} WHILE (condicion)
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "FOR-RANGO" to {
                                onTemplateSelected(
                                    """
FOR (i in 1..5) {
    ${'$'}Contenido
}
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                    // ===== ELEMENTOS =====
                    TemplateDropdownButton(
                        title = "ELEMENTOS",
                        options = listOf(

                            "FILAS" to {
                                onTemplateSelected(
                                    """
    [
        {
            ${'$'}Elemento
        }
    ]
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "COLUMNA" to {
                                onTemplateSelected(
                                    """
    
    {
            ${'$'}Elemento
    }
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },

                            "ORIENTATION" to {
                                onTemplateSelected(
                                    """
    
    orientation: VERTICAL,
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "FUNCION POKEMON" to {
                                onTemplateSelected(
                                    """
    
    who_is_that_pokemon(NUMBER, 1, 5),
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            },
                            "LABEL" to {
                                onTemplateSelected(
                                    """
                                    
    label: "texto",
    
                                """.trimIndent() + "\n"
                                )
                                onDismiss()
                            }
                        )
                    )

                }
            }
        }
    )
}


/*Metodo composable que permite mostrar las opciones como dropdown*/
@Composable
fun TemplateDropdownButton(
    title: String,
    options: List<Pair<String, () -> Unit>>
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF590613)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(title, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color(0xFF121212))
        ) {

            options.forEach { (name, action) ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = name,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    onClick = {
                        expanded = false
                        action()
                    }
                )
            }
        }
    }
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
