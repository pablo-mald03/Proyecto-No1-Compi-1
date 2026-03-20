package com.pablocompany.proyectono1_compi1.data.repository

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledDropQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledMultipleQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledOpenQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests.CompiledSelectQuest
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledSection
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledTable

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
    fun caluclarPuntaje(componentes: List<Any>): Pair<Int, Int> {
        var aciertos = 0
        var totalPreguntas = 0

        // Función interna recursiva para no repetir código
        fun procesarRecursivo(lista: List<Any>) {
            lista.forEach { comp ->
                println("Analizando componente: ${comp::class.simpleName}")

                when (comp) {
                    is CompiledTable -> {
                        val todosLosElementos = comp.elementos?.flatten() ?: emptyList()
                        procesarRecursivo(todosLosElementos)
                    }
                    is CompiledSection -> {
                        procesarRecursivo(comp.elementos ?: emptyList())
                    }
                    is CompiledMultipleQuest -> {
                        totalPreguntas++
                        val id = "${comp.fila}_${comp.columna}"

                        val resUsuario = (_answers[id] as? List<*>)
                            ?.mapNotNull { it?.toString()?.toDoubleOrNull()?.toInt() }
                            ?.sorted() ?: emptyList()

                        val resCorrecta = (comp.respuesta as? List<*>)
                            ?.mapNotNull { it?.toString()?.toDoubleOrNull()?.toInt() }
                            ?.sorted() ?: emptyList()

                        if (resUsuario.isNotEmpty() && resUsuario == resCorrecta) {
                            aciertos++
                        }
                    }
                    is CompiledDropQuest -> {
                        totalPreguntas++
                        val id = "${comp.fila}_${comp.columna}"

                        val resUsuario = _answers[id]?.toString()?.toDoubleOrNull()?.toInt()

                        val resCorrecta = comp.respuesta?.toString()?.toDoubleOrNull()?.toInt()

                        if (resUsuario != null && resUsuario == resCorrecta) {
                            aciertos++
                        }
                    }
                    is CompiledSelectQuest -> {
                        totalPreguntas++
                        val id = "${comp.fila}_${comp.columna}"

                        val resUsuario = _answers[id]?.toString()?.toDoubleOrNull()?.toInt()
                        val resCorrecta = comp.respuesta?.toString()?.toDoubleOrNull()?.toInt()

                        if (resUsuario != null && resUsuario == resCorrecta) {
                            aciertos++
                        }
                    }
                    is CompiledOpenQuest -> {

                        totalPreguntas++
                        val id = "${comp.fila}_${comp.columna}"

                        val respuestaTexto = _answers[id] as? String ?: ""

                        if (respuestaTexto.trim().isNotEmpty()) {
                            aciertos++
                        }
                    }
                }
            }
        }

        procesarRecursivo(componentes)

        return Pair(aciertos, totalPreguntas)
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
