package com.pablocompany.proyectono1_compi1.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

class FormFileRepository(
    private val context: Context
) {

    fun readFile(uri: Uri): String {
        return context.contentResolver
            .openInputStream(uri)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: ""
    }

    fun writeFile(uri: Uri, content: String) {
        context.contentResolver
            .openOutputStream(uri)
            ?.bufferedWriter()
            ?.use { it.write(content) }
    }

    fun getFileName(uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex >= 0) {
                return it.getString(nameIndex)
            }
        }
        return "archivo.form"
    }
}