package com.pablocompany.proyectono1_compi1.data.repository

import android.net.Uri
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
import com.pablocompany.proyectono1_compi1.compiler.logic.sym
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.compiler.models.lexerpintado.TokenUI
import com.pablocompany.proyectono1_compi1.domain.usecase.AnalizarLexicoUseCase
import kotlin.collections.emptyList

class EditorViewModel(
    private val repository: FormFileRepository
) : ViewModel() {

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

    //Metodo que permite reescribir el codigo
    private fun recalcularHighlight() {

        val texto = codeField.text
        if (texto.isEmpty()) {
            highlightedCode = AnnotatedString("")
            listaErrores = emptyList()
            return
        }

        val resultado = analizarLexico(codeField.text)

        highlightedCode = construirAnnotated(resultado.tokens)
        erroresVisuales = resultado.errores
    }

    //Metodo que permite ir coloreando el texto acorde a lo que se requiere
    private fun construirAnnotated(tokens: List<TokenUI>): AnnotatedString {

        return buildAnnotatedString {

            tokens.forEach { token ->

                val color = when (token.tipo) {
                    sym.ID -> Color(0xFFFFFFFF)
                    sym.ENTERO -> Color(0xFF07E3DB)
                    sym.DECIMAL -> Color(0xFFF5426C)
                    else -> Color.White
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
    fun updateCodeField(value: TextFieldValue) {
        codeField = value
        isModified = true
        recalcularHighlight()
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
        recalcularHighlight()
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

    fun ejecutarAnalisisFormal(): Boolean {

        val texto = codeField.text

        if (texto.isEmpty()) {
            listaErrores = emptyList()
            return false
        }

        val resultadoLexico = analizarLexico(texto)

        // Pendiente parser
        // val resultadoParser = analizarSintactico(texto)

        listaErrores = resultadoLexico.errores
        // pendiente concatenar las listas haciendo  resultadoLexico.errores +  resultadoParser.errores

        return listaErrores.isNotEmpty()
    }

    /*====Apartado de analisis formal de codigo al ejecutar====*/

}

