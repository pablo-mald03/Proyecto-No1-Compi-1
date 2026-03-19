package com.pablocompany.proyectono1_compi1.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CodigoInterpretado
import com.pablocompany.proyectono1_compi1.compiler.logic.fuente.LexerCompiled
import com.pablocompany.proyectono1_compi1.compiler.logic.fuente.ParserCompiled
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.data.clases.ResultadoAnalisis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.StringReader

//Clase que permite compartir datos entre pantallas
class SharedFormViewModel : ViewModel() {

    /*Atributos que permiten ejecutar el otro hilo para evitar que crashee la app*/
    private var interpretJob: Job? = null

    var isParsing by mutableStateOf(false)
        private set

    /*Variable que permite guardar el codigo interpretado*/
    var codigoInterpretado by mutableStateOf<CodigoInterpretado?>(null)
        private set

    //Codigo ya procesado que se pasara a la consola de la UI
    var codigoProcesado by mutableStateOf<String>("")
        private set

    // SOLO para análisis de errores (ANALISIS INSTANTANEO)
    var listaErrores by mutableStateOf<List<ErrorAnalisis>>(emptyList())

    var fileName by mutableStateOf("Sin título")
        private set

    var codigoCompilado by mutableStateOf<String?>(null)
        private set

    var currentFileUri by mutableStateOf<Uri?>(null)
        private set

    var isModified by mutableStateOf(false)
        private set

    // Indica si el código se generó desde el editor o desde un archivo
    var generadoDesdeEditor by mutableStateOf(false)
        private set

    //Metodo que permite actualizar el contenido del formulario
    fun loadTemporary(codigoNuevo: String) {
        codigoCompilado = codigoNuevo
        isModified = true
        generadoDesdeEditor = true

        interpretarCodigoCompilado(codigoNuevo)
    }

    //Metodo que permite cargar un archivo
    fun loadFromFile(uri: Uri, contenido: String, name: String) {
        codigoCompilado = contenido
        currentFileUri = uri
        fileName = name
        isModified = false
        generadoDesdeEditor = false

        interpretarCodigoCompilado(contenido)
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

    //Metodo que permite cerrar el formulario abierto
    fun limpiar() {

        interpretJob?.cancel()

        codigoCompilado = null
        codigoInterpretado = null
        codigoProcesado = ""

        listaErrores = emptyList()

        currentFileUri = null
        fileName = "Sin título"

        isModified = false
        generadoDesdeEditor = false

        isParsing = false
    }

    //Metodos utilizados para desmarcar cuando se cerro desde el editor o cuando se guardo desde el editor
    fun marcarDesdeEditor() {
        generadoDesdeEditor = true
    }

    fun desmarcarDesdeEditor() {
        generadoDesdeEditor = false
    }

    //Metodo que permite setear todo lo que trae el useCase
    fun setResultadoAnalisis(resultado: ResultadoAnalisis) {
        codigoProcesado = resultado.codigoGenerado
        listaErrores = resultado.errores
    }

    //Permite limpiar todo para que no quede codigo basura
    fun limpiarResultado() {
        codigoProcesado = ""
        listaErrores = emptyList()
    }

    //Metodo get para obtener el codigo procesado por el parser
    fun getCodigoParaSubir(): String {
        return codigoProcesado
    }

    /*Metodo utilizado para poder interpretar el codigo compilado*/
    private fun interpretarCodigoCompilado(codigo: String) {

        codigoProcesado = codigo

        interpretJob?.cancel()

        interpretJob = viewModelScope.launch {

            isParsing = true

            try {
                val resultado = withContext(Dispatchers.Default) {

                    val reader = StringReader(codigo)
                    val lexer = LexerCompiled(reader)
                    val parser = ParserCompiled(lexer)

                    val parseResult = parser.parse()

                    val interpretado = parseResult.value as? CodigoInterpretado

                    /*Apartado de validar errores*/

                    val errores = parser.syntaxErrorList + lexer.lexicalErrors

                    Pair(interpretado, errores)
                }

                val (interpretado, errores) = resultado

                if (errores.isNotEmpty() || interpretado == null) {
                    codigoInterpretado = null
                    listaErrores = errores
                } else {
                    codigoInterpretado = interpretado
                    listaErrores = emptyList()
                }

            } catch (e: Exception) {
                codigoInterpretado = null
                listaErrores = listOf(
                    ErrorAnalisis(
                        "Interpretacion",
                        "Runtime",
                        e.message ?: "Error interpretando codigo",
                        -1,
                        -1
                    )
                )
            }

            isParsing = false
        }
    }

    /*Metodo para reanalizar el codigo*/
    fun reanalizar() {
        codigoCompilado?.let {
            interpretarCodigoCompilado(it)
        }
    }

}