package com.pablocompany.proyectono1_compi1.data.repository

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditorViewModel(
    private val repository: FormFileRepository
) : ViewModel() {

    var code by mutableStateOf("")
        private set

    var fileName by mutableStateOf<String?>(null)
        private set

    var isModified by mutableStateOf(false)
        private set

    var currentFileUri: Uri? = null
        private set

    fun updateCode(newCode: String) {
        code = newCode
        isModified = true
    }

    fun loadFile(uri: Uri) {
        currentFileUri = uri
        code = repository.readFile(uri)
        fileName = repository.getFileName(uri)
        isModified = false
    }

    fun saveFile() {
        currentFileUri?.let {
            repository.writeFile(it, code)
            isModified = false
        }
    }

    fun saveAs(uri: Uri) {
        repository.writeFile(uri, code)
        currentFileUri = uri
        fileName = repository.getFileName(uri)
        isModified = false
    }

    fun closeFile() {
        currentFileUri = null
        fileName = null
        code = ""
        isModified = false
    }
}

