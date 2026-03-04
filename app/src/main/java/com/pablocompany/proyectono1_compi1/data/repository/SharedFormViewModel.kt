package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedFormViewModel : ViewModel() {

    var codigoCompilado by mutableStateOf<String?>(null)
        private set

    fun setCodigo(codigo: String) {
        codigoCompilado = codigo
    }

    fun limpiar() {
        codigoCompilado = null
    }
}