package com.pablocompany.proyectono1_compi1.domain.modules

import com.pablocompany.proyectono1_compi1.domain.service.FormApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Clase (objeto de kotlin jeje) que permite tener una URL seteable para poderse conectar a la API del servidor de la app
object RetrofitFactory {

    //Funcion que permite setear la baseURL
    fun create(baseUrl: String): FormApiService {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()

            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("ngrok-skip-browser-warning", "true")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(FormApiService::class.java)

    }

}