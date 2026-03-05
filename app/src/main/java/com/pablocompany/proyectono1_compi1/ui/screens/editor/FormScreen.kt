package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel

@Composable
fun FormScreen(
    sharedFormViewModel: SharedFormViewModel
) {

    val codigo = sharedFormViewModel.codigoCompilado
    val isModified = sharedFormViewModel.isModified
    val currentUri = sharedFormViewModel.currentFileUri

    val context = LocalContext.current


    val saveAsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        uri?.let {
            context.contentResolver
                .openOutputStream(it)
                ?.bufferedWriter()
                ?.use { writer ->
                    writer.write(sharedFormViewModel.codigoCompilado ?: "")
                }

            sharedFormViewModel.loadFromFile(it, sharedFormViewModel.codigoCompilado ?: "")
        }
    }

    fun guardarFormulario() {

        if (sharedFormViewModel.currentFileUri != null) {

            context.contentResolver
                .openOutputStream(sharedFormViewModel.currentFileUri!!, "wt")
                ?.bufferedWriter()
                ?.use { writer ->
                    writer.write(sharedFormViewModel.codigoCompilado ?: "")
                }

            sharedFormViewModel.markSaved()

        } else {
            saveAsLauncher.launch("Formulario.pkm")
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (codigo != null) {
            Text(
                text = "Código recibido:\n\n$codigo",
                color = Color.White
            )
        } else {
            Text(
                text = "No hay código cargado",
                color = Color.White
            )
        }
    }

}