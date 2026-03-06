package com.pablocompany.proyectono1_compi1.domain.service

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

//Interface que permite poderse conectar con la API y ya poder generar los respectivos request hacia el servidor
interface FormApiService {

    @Multipart
    @POST("upload")
    suspend fun uploadForm(
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>

}