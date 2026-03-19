package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.State
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

//Viewmodel que se encarga de almacenar todo el codigo que se va a procesar para poder mostrar la vista de contestar el formulario
class AnswerViewModel : ViewModel() {

    /*Atributos que permiten ejecutar el otro hilo para evitar que crashee la app*/
    private var interpretJob: Job? = null

    var isParsing by mutableStateOf(false)
        private set

    //Permite tener el codigo procesado del formulario
    var codigoFormularioState = mutableStateOf("")

    //Atributo que permite tener el codigo interpretado
    var codigoFormularioStateInterprete by mutableStateOf<CodigoInterpretado?>(null)
        private set


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
        codigoFormularioStateInterprete = null
    }

    /*Metodo utilizado para poder interpretar el codigo compilado para poderlo contestar*/
    private fun interpretarCodigoCompilado(codigo: String) {

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
                    codigoFormularioStateInterprete = null
                    listaErrores = errores
                } else {
                    codigoFormularioStateInterprete = interpretado
                    listaErrores = emptyList()
                }

            } catch (e: Exception) {
                codigoFormularioStateInterprete = null
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

    //Metodo que permite procesar el codigo
    fun procesarCodigo(codigo: String) {
        codigoFormularioState.value = codigo
        interpretarCodigoCompilado(codigo)
    }
}