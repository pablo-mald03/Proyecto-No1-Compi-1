package com.pablocompany.proyectono1_compi1.ui.screens.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pablocompany.proyectono1_compi1.data.repository.SharedFormViewModel

@Composable
fun FormScreen(
    sharedFormViewModel: SharedFormViewModel
) {

    val codigo = sharedFormViewModel.codigoCompilado

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