package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledDropQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledMultipleQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledOpenQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledSelectQuest

/*ViewModel que representa a las respuestas del formulario  (FUNCIONAMIENTO BASICO CON HASHMAP)*/
class FormViewModel : ViewModel() {


    /*
    *
    * Se tomo como referencia la fila y la columna para poder determinar la posicion de la respuesta
    * en base a las coordenadas (Aplicacion simple pero efectiva para el caso que no amerita subir respuestas. Sino solo saberlas)
    *
    * */

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


    /*Metodo utilizado para poder calificar las respuestas obtenidas en el resultado*/
    fun caluclarPuntaje(componentes: List<Any>): Pair<Int,Int> {

        var aciertos = 0
        var totalCalificables = 0

        componentes.forEach { comp ->

            val id = when (comp) {
                is CompiledOpenQuest -> "${comp.fila}_${comp.columna}"
                is CompiledSelectQuest -> "${comp.fila}_${comp.columna}"
                is CompiledDropQuest -> "${comp.fila}_${comp.columna}"
                is CompiledMultipleQuest -> "${comp.fila}_${comp.columna}"
                else -> null
            }


            if (id != null) {
                val respuestaUsuario = _answers[id]

                when (comp) {
                    is CompiledMultipleQuest -> {
                        totalCalificables++
                        val userList =
                            (respuestaUsuario as? List<*>)?.mapNotNull { it as? Int }?.sorted()
                        val correctList =
                            (comp.respuesta as? List<*>)?.mapNotNull { it as? Int }?.sorted()
                        if (userList != null && userList == correctList){
                            aciertos++
                        }
                    }
                    is CompiledSelectQuest -> {

                        totalCalificables++

                        if (respuestaUsuario == comp.respuesta){
                            aciertos++
                        }
                    }
                    is CompiledDropQuest -> {

                        totalCalificables++
                        if (respuestaUsuario == comp.respuesta){
                            aciertos++
                        }
                    }
                    is CompiledOpenQuest -> {

                        totalCalificables++
                        aciertos++
                    }
                }

            }

        }
        return Pair(aciertos, totalCalificables)
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
