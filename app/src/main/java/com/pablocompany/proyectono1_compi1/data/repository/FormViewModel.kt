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
import com.pablocompany.proyectono1_compi1.data.clases.EvaluacionQuestion
import com.pablocompany.proyectono1_compi1.data.clases.ReporteFormulario
import com.pablocompany.proyectono1_compi1.ui.screens.editor.toDisplayString

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
    fun calcularReporteDetallado(
        componentes: List<Any>,
        respuestasUsuario: Map<String, Any>
    ): ReporteFormulario {
        val evaluacionMap = mutableMapOf<String, EvaluacionQuestion>()

        fun procesar(lista: List<Any>) {
            lista.forEach { comp ->
                when (comp) {
                    is CompiledTable -> procesar(comp.elementos?.flatten() ?: emptyList())
                    is CompiledSection -> procesar(comp.elementos ?: emptyList())

                    is CompiledSelectQuest, is CompiledDropQuest -> {
                        // Extraemos datos según el tipo
                        val fila = if (comp is CompiledSelectQuest) comp.fila else (comp as CompiledDropQuest).fila
                        val col = if (comp is CompiledSelectQuest) comp.columna else (comp as CompiledDropQuest).columna
                        val texto = if (comp is CompiledSelectQuest) comp.texto.toDisplayString() else (comp as CompiledDropQuest).texto.toDisplayString()
                        val resCorrectaRaw = if (comp is CompiledSelectQuest) comp.respuesta else (comp as CompiledDropQuest).respuesta

                        val id = "${fila}_${col}"
                        val evaluacion = evaluarSimple(fila, col, texto, resCorrectaRaw, respuestasUsuario)

                        // Guardamos en el mapa usando el ID como llave
                        evaluacionMap[id] = evaluacion
                    }

                    is CompiledOpenQuest -> {
                        val id = "${comp.fila}_${comp.columna}"
                        val res = respuestasUsuario[id]?.toString() ?: ""
                        val esBien = res.trim().isNotEmpty()
                        evaluacionMap[id] = EvaluacionQuestion(
                            id = id,
                            titulo = comp.texto.toDisplayString(),
                            respuestaUsuario = res,
                            respuestaCorrecta = "Abierta",
                            esCorrecta = esBien,
                            esInformativa = true
                        )
                    }

                    is CompiledMultipleQuest -> {
                        val id = "${comp.fila}_${comp.columna}"
                        val resU = (respuestasUsuario[id] as? List<*>)?.mapNotNull { it.toString().toDoubleOrNull()?.toInt() }?.sorted() ?: emptyList()
                        val resC = (comp.respuesta as? List<*>)?.mapNotNull { it.toString().toDoubleOrNull()?.toInt() }?.sorted() ?: emptyList()

                        val esInformativa = resC.isEmpty()
                        val esCorrecta = if (esInformativa) resU.isNotEmpty() else resU == resC

                        evaluacionMap[id] = EvaluacionQuestion(id, "Pregunta Múltiple", resU, resC, esCorrecta, esInformativa)
                    }
                }
            }
        }

        procesar(componentes)

        val aciertos = evaluacionMap.values.count { it.esCorrecta }
        val total = evaluacionMap.size

        return ReporteFormulario(
            detalles = evaluacionMap,
            aciertos = aciertos,
            total = total,
            porcentaje = if(total > 0) (aciertos.toFloat() / total * 100) else 0f
        )
    }

    /*Funcion auxiliar que permite validar si la respuesta es correcta simple*/
    private fun evaluarSimple(fila: Int, col: Int, titulo: String, respuestaC: Any?, respuestasU: Map<String, Any>):  EvaluacionQuestion {
        val id = "${fila}_${col}"
        val respuestaUnitaria = respuestasU[id]?.toString()?.toDoubleOrNull()?.toInt()
        val respuestaCorrecta = respuestaC?.toString()?.toDoubleOrNull()?.toInt() ?: -1

        val esInformativa = respuestaC == -1
        val esCorrecta = if (esInformativa) respuestaUnitaria != null else respuestaUnitaria == respuestaCorrecta

        return EvaluacionQuestion(id, titulo, respuestaUnitaria, respuestaCorrecta, esCorrecta, esInformativa)
    }

    /*Funcion que permite limpiar el hash*/
    fun clear() {
        _answers.clear()
    }

    fun getAllAnswers(): Map<String, Any> = _answers.toMap()


}
