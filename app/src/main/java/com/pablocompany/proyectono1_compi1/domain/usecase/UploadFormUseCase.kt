package com.pablocompany.proyectono1_compi1.domain.usecase

import android.content.Context
import android.net.Uri
import com.pablocompany.proyectono1_compi1.domain.modules.NetworkModule
import com.pablocompany.proyectono1_compi1.domain.service.FormApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Part

class UploadFormUseCase(
    private val context: Context
) {

    //Metodo para hacer el request cuando ya tengo un archivo guardado local
    suspend fun uploadForm(
        api: FormApiService,
        fileUri: Uri,
        fileName: String
    ): Boolean {

        return try {

            val inputStream = context.contentResolver.openInputStream(fileUri)
                ?: return false

            val requestFile = inputStream.use {
                it.readBytes().toRequestBody("application/octet-stream".toMediaType())
            }

            val multipart = MultipartBody.Part.createFormData(
                "formulario",
                "$fileName.pkm",
                requestFile
            )

            val autor = "app-movil"
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val response = api.uploadForm(
                autor = autor,
                formulario = multipart
            )

            response.isSuccessful

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //Metodo para hacer el request cuando NO SE TIENE GUARDADO LOCAL
    suspend fun uploadFormContent(
        api: FormApiService,
        content: String,
        fileName: String
    ): Boolean {

        return try {

            val requestFile = content
                .toRequestBody("application/octet-stream".toMediaType())

            val multipart = MultipartBody.Part.createFormData(
                "formulario",
                "$fileName.pkm",
                requestFile
            )

            val autor = "app-movil"
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val response = api.uploadForm(
                autor = autor,
                formulario = multipart
            )

            response.isSuccessful

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}