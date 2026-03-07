package com.pablocompany.proyectono1_compi1.domain.service

import com.pablocompany.proyectono1_compi1.data.clases.FormContentDTO
import com.pablocompany.proyectono1_compi1.data.clases.FormServer
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

//Interface que permite poderse conectar con la API y ya poder generar los respectivos request hacia el servidor
interface FormApiService {

    //Metodo que permite ir listando los formularios que estan subidos al servidor (paginacion)
    @GET("forms/uploaded/limit/{limit}/offset/{offset}")
    suspend fun getForms(
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Response<List<FormServer>>

    //Metodo que permite retornar SOLO EL CONTENIDO DEL FORMULARIO
    @GET("forms/content/{id}")
    suspend fun getFormContent(
        @Path("id") id: String
    ): Response<FormContentDTO>

    //Metodo que permite la descarga del archivo desde el servidor
    @GET("forms/descargar/{id}")
    suspend fun downloadForm(
        @Path("id") id: String
    ): Response<ResponseBody>

    //Metodo POST que permite subir los archivos al servidor
    @Multipart
    @POST("forms")
    suspend fun uploadForm(
        @Part("autor") autor: RequestBody,
        @Part formulario: MultipartBody.Part
    ): Response<ResponseBody>

}