package com.pablocompany.proyectono1_compi1.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pablocompany.proyectono1_compi1.data.repository.EditorViewModel
import com.pablocompany.proyectono1_compi1.data.repository.EditorViewModelFactory
import com.pablocompany.proyectono1_compi1.data.repository.FormFileRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    val code by viewModel::code
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
        uri?.let {
            viewModel.loadFile(it)
        }
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
                TextButton(
                    onClick = {
                        showUnsavedDialog = false
                        pendingAction?.invoke()
                    }
                ) {
                    Text("Continuar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showUnsavedDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
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
                        pendingAction = {
                            viewModel.closeFile()
                        }
                        showUnsavedDialog = true
                    } else {
                        viewModel.closeFile()
                    }
                },
                currentFileUri = viewModel.currentFileUri,
                isModified = viewModel.isModified,
                fileName = fileName
            )
        }
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
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
                                        scope.launch { drawerState.open() }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = null
                                    )
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
                    /*CODIGO QUEMADO*/
                    repeat(10) { index ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2C2C2C)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
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

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 36.dp,
                sheetContainerColor = Color(0xFF1E1E1E),
                containerColor = Color.Transparent,
                sheetContent = {
                    ConsoleSection(
                        code = code,
                        onCodeChange = { viewModel.updateCode(it) },
                        onGuardarClick = {
                            if (viewModel.currentFileUri != null) {
                                viewModel.saveFile()
                            } else {
                                saveAsLauncher.launch("archivo.form")
                            }
                        },
                        onEjecutarClick = {
                            println("Ejecutar análisis")
                        },
                        onAgregarClick = {
                            println("Agregar elemento")
                        }
                    )
                },
                modifier = Modifier.matchParentSize()
            ) {}
        }
    }
}

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

@Composable
fun ConsoleSection(
    code: String,
    onCodeChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    onEjecutarClick: () -> Unit,
    onAgregarClick: () -> Unit
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

            TextField(
                value = code,
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