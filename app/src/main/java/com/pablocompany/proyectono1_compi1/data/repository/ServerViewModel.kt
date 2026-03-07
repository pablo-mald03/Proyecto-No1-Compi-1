package com.pablocompany.proyectono1_compi1.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablocompany.proyectono1_compi1.data.clases.FormServer
import com.pablocompany.proyectono1_compi1.domain.modules.RetrofitFactory
import com.pablocompany.proyectono1_compi1.domain.service.FormApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

//ViewModel que permite poder generar los requests y toda la interaccion automatica que hace la app al conectar con el service
class ServerViewModel : ViewModel() {

    //Atributo que permite generar la URL seteable
    private var api: FormApiService? = null

    //Listado de formularios que se llenan con el request
    var formularios by mutableStateOf<List<FormServer>>(emptyList())
        private set

    //Variable que permite saber si se esta cargando o no
    var loading by mutableStateOf(false)
        private set

    //Variable temporal de errores SIGUE SIENDO TEMPORAL
    var error by mutableStateOf<String?>(null)
        private set

    //METODO QUE PERMITE SETEAR LA URL PARA PODER GENERAR LA CONEXION A LA API
    fun setBaseUrl(url: String) {

        try {

            api = RetrofitFactory.create(url)

        } catch (e: Exception) {

            error = "URL inválida"

        }
    }

    //METODO QUE PERMITE TESTEAR SI LA URL ES HACIA LA API DEL SERVIDOR (EL ERROR SIGNIFICA QUE NO LO ES)
    fun testConnection(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response = api?.getForms(1,0)

                if (response?.isSuccessful == true) {

                    onSuccess()

                } else {

                    val errorBody = response?.errorBody()?.string()

                    if (errorBody?.contains("ERR_NGROK") == true) {

                        error = "Ngrok bloqueo la conexión"

                    }

                    onError()
                }

            } catch (e: Exception) {

                error = "No se pudo conectar al servidor"

                onError()
            }
        }
    }

    //METODO QUE PERMITE OBTENER LOS ARCHIVOS ALMACENADOS EL SERVIDOR (PAGINACION)
    fun getForms() {

        viewModelScope.launch {

            loading = true
            error = null

            try {

                val response = api?.getForms(10,0)

                if(response?.isSuccessful == true){

                    formularios = response.body() ?: emptyList()

                }else{

                    error = response?.let { extractErrorMessage(it) } ?: "Error desconocido"

                }

            } catch (e: Exception) {

                error = "No se pudo conectar al servidor"

            }
            loading = false
        }
    }

    /* ================= METODO QUE PERMITE DESCARGAR EL ARCHIVO ================= */
    suspend fun downloadForm(id: String): ResponseBody? {

        return try {

            val response = api?.downloadForm(id)

            if(response?.isSuccessful == true){

                response.body()

            }else null

        } catch (e: Exception) {

            null
        }
    }


    /* ================= METODO QUE PERMITE TRAER CONTENIDO FORM PARA PODERLO CONTESTAR DE UNA VEZ ================= */
    suspend fun getFormContent(id: String): String? {

        return try {

            val response = api?.getFormContent(id)

            if(response?.isSuccessful == true){

                response.body()

            }else null

        } catch (e: Exception) {

            null
        }
    }

    /*==========METODO QUE PERMITE LEER LOS ERRORES QUE REALMENTE RETORNA EL SERVIDOR :) (POR ESO MI INTEGRACION DE API REST)==========*/
    private fun extractErrorMessage(response: Response<*>): String {

        return try {

            response.errorBody()?.string() ?: "Error desconocido"

        } catch (e: Exception) {

            "Error desconocido"
        }
    }

}