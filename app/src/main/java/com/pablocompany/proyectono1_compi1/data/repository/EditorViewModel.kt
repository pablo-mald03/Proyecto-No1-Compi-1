package com.pablocompany.proyectono1_compi1.data.repository

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class EditorViewModel(
    private val repository: FormFileRepository
) : ViewModel() {

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
    }

    fun loadFile(uri: Uri) {
        currentFileUri = uri
        val content = repository.readFile(uri)

        codeField = TextFieldValue(
            text = content,
            selection = TextRange(content.length)
        )

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
        isModified = false
    }
}

