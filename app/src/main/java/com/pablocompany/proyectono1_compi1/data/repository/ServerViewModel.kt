package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablocompany.proyectono1_compi1.data.clases.FormServer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

//ViewModel que permite poder generar los requests y toda la interaccion automatica que hace la app al conectar con el service
class ServerViewModel : ViewModel() {

    //Listado de formularios que se llenan con el request
    var formularios by mutableStateOf<List<FormServer>>(emptyList())
        private set

    //Variable que permite saber si se esta cargando o no
    var loading by mutableStateOf(false)
        private set

    //Variable temporal de errores
    var error by mutableStateOf<String?>(null)
        private set


    fun getForms() {

        viewModelScope.launch {

            loading = true
            error = null

            try {

                // -----------------------------
                // PENDIENTE CONECTAR API

                /*
                val response = api.getForms()

                if(response.isSuccessful){

                    formularios = response.body() ?: emptyList()

                }else{
                    error = "Error del servidor"
                }
                */

                //DELAY MUY MUY TEMPORAL
                delay(600)

                formularios = listOf(
                    FormServer(  "1","Pablo Maldonado","Examen Matemática", "05/03/2026", "22:31"),
                    FormServer(  "2","Ana López", "Evaluación Física", "04/03/2026", "18:20"),
                    FormServer(  "3","Carlos Ruiz","Quiz Compi", "03/03/2026", "15:05")
                )

            } catch (e: Exception) {
                error = "No se pudo conectar al servidor"

            }

            loading = false
        }

    }

    /* ================= METODO QUE PERMITE DESCARGAR EL ARCHIVO ================= */
    suspend fun downloadForm(id: String): ResponseBody? {

        return try {

            //MUY MUY PENDIENTE LA INTEGRACION CON LA API
            /*
            val response = api.downloadForm(id)

            if(response.isSuccessful){
                response.body()
            }else null
            */

            null

        } catch (e: Exception) {
            //Pendiente
            null
        }
    }


    /* ================= METODO QUE PERMITE TRAER CONTENIDO FORM PARA PODERLO CONTESTAR DE UNA VEZ ================= */
    suspend fun getFormContent(id: String): String? {

        return try {

            //CODIGO QUEMADO TEMPORAL
            /*
            val response = api.getFormContent(id)

            if(response.isSuccessful){
                response.body()?.string()
            }else null
            */

            "FORMULARIO DESCARGADO DESDE EL SERVER"

        } catch (e: Exception) {
            null
        }
    }


}