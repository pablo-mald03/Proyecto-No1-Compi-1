package com.pablocompany.proyectono1_compi1.data.repository

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedFormViewModel : ViewModel() {

    var codigoCompilado by mutableStateOf<String?>(null)
        private set

    var currentFileUri by mutableStateOf<Uri?>(null)
        private set

    var isModified by mutableStateOf(false)
        private set

    // Indica si el código se generó desde el editor o desde un archivo
    var generadoDesdeEditor by mutableStateOf(false)
        private set

    fun loadTemporary(codigoNuevo: String) {
        codigoCompilado = codigoNuevo
        isModified = true
        generadoDesdeEditor = true
    }

    fun loadFromFile(uri: Uri, contenido: String) {
        codigoCompilado = contenido
        currentFileUri = uri
        isModified = false
        generadoDesdeEditor = false
    }

    //Exclusivo del editorScreen
    fun updateCodigoEditor(nuevo: String) {
        codigoCompilado = nuevo
        isModified = true
        generadoDesdeEditor = true
    }

    //Permite guardar desde el formulario
    fun updateCodigoForm(nuevo: String) {
        codigoCompilado = nuevo
        isModified = true
    }

    fun markSaved() {
        isModified = false
    }

    fun limpiar() {
        codigoCompilado = null
        currentFileUri = null
        isModified = false
        generadoDesdeEditor = false
    }

    //Metodos utilizados para desmarcar cuando se cerro desde el editor o cuando se guardo desde el editor
    fun marcarDesdeEditor() {
        generadoDesdeEditor = true
    }

    fun desmarcarDesdeEditor() {
        generadoDesdeEditor = false
    }
}