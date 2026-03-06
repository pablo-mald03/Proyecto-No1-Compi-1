package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

//Viewmodel que se encarga de almacenar todo el codigo que se va a procesar para poder mostrar la vista de contestar el formulario
class AnswerViewModel : ViewModel(){

    private val codigoFormulario = mutableStateOf("")

    val codigoFormularioState: State<String> = codigoFormulario

    //Metodo que sirve para setear el codigo que viene (ya solo para contestar el formulario)
    fun setCodigoProcesado(codigo: String) {
        codigoFormulario.value = codigo
    }
}