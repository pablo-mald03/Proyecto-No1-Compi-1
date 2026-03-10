package com.pablocompany.proyectono1_compi1.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.NodoCodigo
import com.pablocompany.proyectono1_compi1.compiler.logic.formulario.LexerForms
import com.pablocompany.proyectono1_compi1.compiler.logic.formulario.ParserForms
import com.pablocompany.proyectono1_compi1.compiler.logic.formulario.sym
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.compiler.models.lexerpintado.TokenUI
import com.pablocompany.proyectono1_compi1.domain.usecase.AnalizarLexicoUseCase
import kotlin.collections.emptyList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.StringReader

class EditorViewModel(
    private val repository: FormFileRepository
) : ViewModel() {

    //Variable que optimiza el pintado de letras
    private var highlightJob: Job? = null


    /*======Apartado de codigo que permite generar el coloreado dinamico de codigo=======*/
    //Lexer
    private val analizarLexico = AnalizarLexicoUseCase()

    // Parser
    //private val analizarSintactico = AnalizarSintacticoUseCase()

    var highlightedCode by mutableStateOf(AnnotatedString(""))
        private set

    // SOLO para análisis formal (boton)
    var listaErrores by mutableStateOf<List<ErrorAnalisis>>(emptyList())
        private set

    // SOLO para highlight visual (tiempo real)
    private var erroresVisuales: List<ErrorAnalisis> = emptyList()

    // Código generado por backend listo para siguiente fase
    var codigoGenerado by mutableStateOf<String?>(null)
        private set

    //Metodo que optimiza el pintado dinamico del codigo
    private fun recalcularHighlightDebounced() {

        highlightJob?.cancel()

        highlightJob = viewModelScope.launch {

            delay(80)
            recalcularHighlight()
        }
    }

    //Metodo que permite reescribir el codigo
    private fun recalcularHighlight() {
        val textoActual = codeField.text
        if (textoActual.isEmpty()) {
            highlightedCode = AnnotatedString("")
            return
        }

        val resultado = analizarLexico(textoActual)

        val nuevoAnnotated = construirAnnotated(resultado.tokens)

        if (nuevoAnnotated.text == codeField.text) {
            highlightedCode = nuevoAnnotated
        } else {
            highlightedCode = AnnotatedString(codeField.text)
        }

        erroresVisuales = resultado.errores
    }

    //Metodo que permite ir coloreando el texto acorde a lo que se requiere
    private fun construirAnnotated(tokens: List<TokenUI>): AnnotatedString {
        return buildAnnotatedString {
            tokens.forEach { token ->
                val color = when (token.tipo) {
                    sym.ERROR -> Color(0xFFB90101)
                    sym.ID -> Color(0xFFFFFFFF)
                    sym.SUMA, sym.RESTA, sym.MULTIPLICACION, sym.DIVISION,
                    sym.POTENCIA, sym.MODULO -> Color(0xFF1AD305)

                    sym.VAR_ESPECIAL, sym.VAR_NUMERO, sym.VAR_STRING,sym.FOR,sym.IF,sym.ELSE,sym.WHILE,sym.DO,sym.IN -> Color(0xFF6602F1)

                    sym.ENTERO, sym.DECIMAL -> Color(0xFF07E3DB)

                    sym.INICIO_CADENA, sym.FIN_CADENA, sym.TEXTO_PLANO -> Color(0xFFE17C02)

                    sym.EMOJI_SMILE, sym.EMOJI_SAD, sym.EMOJI_SERIOUS,
                    sym.EMOJI_HEART, sym.EMOJI_STAR, sym.EMOJI_MULTI_STAR,
                    sym.EMOJI_CAT -> Color(0xFFE1C302)

                    sym.PARENT_CIERRE, sym.PARENT_APERTURA, sym.CORCHETE_CIERRE,
                    sym.CORCHETE_APERTURA, sym.LLAVE_APERTURA, sym.LLAVE_CIERRE -> Color(0xFF073EE3)
                    sym.COMENTARIO_TEXTO -> Color(0xFF7E7D7D)


                    else -> Color(0xFFFCF5C4)
                }
                withStyle(SpanStyle(color = color)) {
                    append(token.lexema)
                }
            }
        }
    }

    /*======Apartado de codigo que permite generar el coloreado dinamico de codigo=======*/


    /*======Apartado de codigo que permite generar persistencia de datos=======*/
    var codeField by mutableStateOf(TextFieldValue(""))
        private set

    val code: String
        get() = codeField.text

    var fileName by mutableStateOf<String?>(null)
        private set

    var isModified by mutableStateOf(false)
        private set

    var currentFileUri: Uri? = null
        private set

    //Metodo que permite ir subrayando a tiempo real mientras se escriba
    fun updateCodeField(newValue: TextFieldValue) {
        val oldText = codeField.text
        val newText = newValue.text

        val oldAnnotated = highlightedCode

        codeField = newValue
        isModified = true

        if (oldText != newValue.text) {
            highlightedCode = if (newText.length > oldText.length && oldAnnotated.text.isNotEmpty()) {
                buildAnnotatedString {
                    append(newText)
                    oldAnnotated.spanStyles.forEach { style ->
                        if (style.end <= newText.length) {
                            addStyle(style.item, style.start, style.end)
                        }
                    }
                }
            } else {
                AnnotatedString(newText)
            }
            recalcularHighlightDebounced()
        }
    }

    //Metodo que permite parchear automaticamente
    private fun patchAnnotatedString(old: AnnotatedString, newValue: TextFieldValue): AnnotatedString {
        if (old.text.length == newValue.text.length) return old

        return buildAnnotatedString {
            append(newValue.text)
        }
    }


    //Metodo que permite insertar texto en cualquier lugar del codigo en base al cursor
    fun insertTextAtCursor(text: String) {
        val current = codeField

        val newText = buildString {
            append(current.text.substring(0, current.selection.start))
            append(text)
            append(current.text.substring(current.selection.end))
        }

        codeField = TextFieldValue(
            text = newText,
            selection = TextRange(current.selection.start + text.length)
        )

        isModified = true
        recalcularHighlightDebounced()
    }

    //Metodo que permite cargar un archivo de entrada
    fun loadFile(uri: Uri) {
        currentFileUri = uri
        val content = repository.readFile(uri)

        codeField = TextFieldValue(
            text = content,
            selection = TextRange(content.length)
        )
        recalcularHighlight()
        fileName = repository.getFileName(uri)
        isModified = false
    }

    //Metodo que permite guardar un archivo
    fun saveFile() {
        currentFileUri?.let {
            repository.writeFile(it, code)
            isModified = false
        }
    }

    //Metodo que permite elegir donde guardar un archivo
    fun saveAs(uri: Uri?) {
        if (uri == null) return

        repository.writeFile(uri, code)
        currentFileUri = uri
        fileName = repository.getFileName(uri)
        isModified = false
    }

    //Metodo que cierra el archivo
    fun closeFile() {
        currentFileUri = null
        fileName = null
        codeField = TextFieldValue("")
        highlightedCode = AnnotatedString("")
        listaErrores = emptyList()
        isModified = false

    }

    /*======Apartado de codigo que permite generar persistencia de datos=======*/

    /*====Apartado de analisis formal de codigo al ejecutar====*/

    //Metodo encargado de generar la compilacion del codigo o pre-compilacion
    private fun analizarCompleto(texto: String): Pair<List<ErrorAnalisis>, String?> {

        if (texto.isBlank()) {
            return Pair(emptyList(), null)
        }

        val resultadoLexico = analizarLexico(texto)

        val (erroresParser, ast) = analizarSintactico(texto)

        val erroresTotales = resultadoLexico.errores + erroresParser

        if (erroresTotales.isNotEmpty()) {
            return Pair(erroresTotales, null)
        }

        Log.d("AST", "===== AST GENERADO =====")
        Log.d("AST1", ast?.toString() ?: "AST NULL")

        // TEMPORAL
        val codigoGenerado = texto

        return Pair(emptyList(), codigoGenerado)
    }

    //Metodo que permite ejecutar el analisis formal del parser
    private fun analizarSintactico(texto: String): Pair<List<ErrorAnalisis>, NodoCodigo?> {

        return try {

            val reader = StringReader(texto)

            val lexer = LexerForms(reader,true)

            val parser = ParserForms(lexer)

            val resultado = parser.parse()

            val ast = resultado.value as? NodoCodigo

            val erroresParser = parser.syntaxErrorList

            Pair(erroresParser, ast)

        } catch (ex: Exception) {

            val errores = listOf(
                ErrorAnalisis(
                    "Desconocido",
                    "Sintáctico",
                    ex.message ?: "Error desconocido en parser",
                    -1,
                    -1
                )
            )

            Pair(errores, null)
        }
    }



    //METODO QUE GENERA EL PRE-COMPILADO PARA ACTUALIZAR LA PREVISUALIZACION
    fun ejecutarAnalisisFormal(): Boolean {

        val (errores, _) = analizarCompleto(codeField.text)

        listaErrores = errores

        return errores.isNotEmpty()
    }

    //Metodo encargado de compilar el codigo y hacer TODA LA LOGICA BACKEND QUE CONLLEVA
    fun compilarFormulario(): Boolean {

        val (errores, codigo) = analizarCompleto(codeField.text)

        listaErrores = errores

        if (errores.isNotEmpty()) {
            codigoGenerado = null
            return false
        }

        codigoGenerado = codigo
        isModified = false
        return true
    }


    /*====Apartado de analisis formal de codigo al ejecutar====*/

    //Metodo para limpiar codigo
    fun limpiarCodigo() {
        highlightJob?.cancel()
        codeField = TextFieldValue("")
        highlightedCode = AnnotatedString("")
        listaErrores = emptyList()
        codigoGenerado = null
        isModified = true
    }

}

