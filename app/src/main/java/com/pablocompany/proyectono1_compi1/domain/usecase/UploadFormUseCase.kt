package com.pablocompany.proyectono1_compi1.domain.usecase

import android.content.Context
import android.net.Uri
import com.pablocompany.proyectono1_compi1.domain.modules.NetworkModule
import com.pablocompany.proyectono1_compi1.domain.service.FormApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Part

class UploadFormUseCase(
    private val context: Context
) {

    private val api = NetworkModule.retrofit.create(FormApiService::class.java)

    //Metodo para hacer el request cuando ya tengo un archivo guardado local
    suspend fun uploadForm(
        fileUri: Uri,
        fileName: String
    ): Boolean {

        return try {
/*
            val inputStream = context.contentResolver.openInputStream(fileUri)
                ?: return false

            val requestFile = inputStream.use {
                it.readBytes().toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val multipart = MultipartBody.Part.createFormData(
                "file",
                "$fileName.txt",
                requestFile
            )

            val response = api.uploadForm(multipart)

            response.isSuccessful*/

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //Metodo para hacer el request cuando NO SE TIENE GUARDADO LOCAL
    suspend fun uploadFormContent(
        content: String,
        fileName: String
    ): Boolean {

        return try {

            /*val requestFile = content
                .toRequestBody("text/plain".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData(
                "file",
                "$fileName.txt",
                requestFile
            )

            val response = api.uploadForm(multipart)

            response.isSuccessful*/

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}