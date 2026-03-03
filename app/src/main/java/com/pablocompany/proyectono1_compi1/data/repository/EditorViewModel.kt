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
    private val analizarLexico = AnalizarLexicoUseCase()

    var highlightedCode by mutableStateOf(AnnotatedString(""))
        private set

    var listaErrores by mutableStateOf<List<ErrorAnalisis>>(emptyList())
        private set

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
        listaErrores = resultado.errores
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

    fun updateCodeField(value: TextFieldValue) {
        codeField = value
        isModified = true
        recalcularHighlight()
    }

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

    fun saveFile() {
        currentFileUri?.let {
            repository.writeFile(it, code)
            isModified = false
        }
    }

    fun saveAs(uri: Uri?) {
        if (uri == null) return

        repository.writeFile(uri, code)
        currentFileUri = uri
        fileName = repository.getFileName(uri)
        isModified = false
    }

    fun closeFile() {
        currentFileUri = null
        fileName = null
        codeField = TextFieldValue("")
        highlightedCode = AnnotatedString("")
        listaErrores = emptyList()
        isModified = false

    }

    /*======Apartado de codigo que permite generar persistencia de datos=======*/

}

