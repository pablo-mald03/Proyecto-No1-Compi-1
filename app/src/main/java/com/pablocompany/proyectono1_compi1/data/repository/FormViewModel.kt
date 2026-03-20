package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

/*ViewModel que representa a las respuestas del formulario  (FUNCIONAMIENTO BASICO CON HASHMAP)*/
class FormViewModel : ViewModel() {

    /*Hashmap que guarda los valores de las tablas*/
    private val _answers = mutableStateMapOf<String, Any>()
    val answers: Map<String, Any> = _answers


    /*Metodo que permite guardar la respuesta*/
    fun setAnswer(id: String, value: Any) {
        _answers[id] = value
    }

    /*Metodo que permite obtener la respuesta*/
    fun getAnswer(id: String): Any? {
        return _answers[id]
    }

    /*Metodo que permite validar si la respuesta esta correcta*/
    fun isValid(id: String): Boolean {
        val value = answers[id] as? String
        return !value.isNullOrBlank()
    }

    /*Funcion que permite limpiar el hash*/
    fun clear() {
        _answers.clear()
    }


}
