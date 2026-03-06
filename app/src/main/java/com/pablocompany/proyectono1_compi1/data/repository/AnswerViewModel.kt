package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.data.clases.ResultadoAnalisis

//Viewmodel que se encarga de almacenar todo el codigo que se va a procesar para poder mostrar la vista de contestar el formulario
class AnswerViewModel : ViewModel() {

    //Permite tener el codigo procesado del formulario
    var codigoFormularioState = mutableStateOf("")

    //Lista de errores (para poder compilar siempre es importante verificar si esta bien escrito)

    var listaErrores by mutableStateOf<List<ErrorAnalisis>>(emptyList())
        private set

    //Metodo que sirve para setear el codigo que viene (ya solo para contestar el formulario)
    fun setCodigoProcesado(codigo: String) {
        codigoFormularioState.value = codigo
    }

    //Permite setear el resultado de analisis
    fun setResultadoAnalisis(resultado: ResultadoAnalisis) {
        codigoFormularioState.value = resultado.codigoGenerado
        listaErrores = resultado.errores
    }

    //Permite limpiar el resultado
    fun limpiarResultado() {
        codigoFormularioState.value = ""
        listaErrores = emptyList()
    }
}