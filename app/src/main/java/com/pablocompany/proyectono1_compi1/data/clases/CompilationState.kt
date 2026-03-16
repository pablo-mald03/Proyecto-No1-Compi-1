package com.pablocompany.proyectono1_compi1.data.clases

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis

//Metodo que permite modificar el hilo principal para poder hacer los requests a la POKEAPI
sealed class CompilacionState {
    object Idle : CompilacionState()
    object Loading : CompilacionState()
    data class Success(val codigo: String) : CompilacionState()
    data class Error(val errores: List<ErrorAnalisis>) : CompilacionState()
}