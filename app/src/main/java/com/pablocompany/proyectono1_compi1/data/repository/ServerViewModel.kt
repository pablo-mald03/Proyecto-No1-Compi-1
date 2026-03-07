package com.pablocompany.proyectono1_compi1.data.repository

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablocompany.proyectono1_compi1.data.clases.FormServer
import com.pablocompany.proyectono1_compi1.domain.modules.RetrofitFactory
import com.pablocompany.proyectono1_compi1.domain.service.FormApiService
import com.pablocompany.proyectono1_compi1.domain.usecase.UploadFormUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

//ViewModel que permite poder generar los requests y toda la interaccion automatica que hace la app al conectar con el service
class ServerViewModel(application: Application) : AndroidViewModel(application){

    //Constante que interconecta con el usecase para hacer el POST
    private val uploadUseCase = UploadFormUseCase(getApplication())

    //Atributo que permite generar la URL seteable
    private var api: FormApiService? = null

    //Atributo que define de forma global la url de la api
    var baseUrl: String? = null
        private set

    //Atributo que define si se puede seguir cargando mas (si ya no hay mas en la db bloquear el cargar mas)
    var hasMore by mutableStateOf(true)
        private set

    //Listado de formularios que se llenan con el request
    var formularios by mutableStateOf<List<FormServer>>(emptyList())
        private set

    //Variable que permite saber si se esta cargando o no
    var loading by mutableStateOf(false)
        private set

    //Variable temporal de errores SIGUE SIENDO TEMPORAL
    var error by mutableStateOf<String?>(null)
        private set

    //Variables de paginacion
    private val pageSize = 5
    private var offset = 0

    //Metodo que permite retornar la conexion a la API
    fun getApi(): FormApiService? {
        return api
    }

    //Metodo que permite reiniciar la paginacion
    fun resetPaginacion(){
        offset = 0
        hasMore = true
        formularios = emptyList()

    }

    //METODO QUE PERMITE SETEAR LA URL PARA PODER GENERAR LA CONEXION A LA API
    fun setBaseUrl(url: String) {

        if(url.isBlank()){
            error = "La URL no puede estar vacía"
            return
        }

        try {

            baseUrl = url
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

        offset = 0

        viewModelScope.launch {

            loading = true
            error = null

            try {

                val response = api?.getForms(pageSize, offset)

                if(response?.isSuccessful == true){

                    val nuevos = response.body() ?: emptyList()

                    formularios = nuevos

                    hasMore = nuevos.size == pageSize

                }else{

                    error = response?.let { extractErrorMessage(it) } ?: "Error desconocido"

                }

            } catch (e: Exception) {

                error = "No se pudo conectar al servidor"

            }

            loading = false
        }
    }

    /*====METODO QUE PERMITE IR PAGINANDO=====*/
    fun loadMoreForms() {

        viewModelScope.launch {

            try {

                val newOffset = offset + pageSize

                val response = api?.getForms(pageSize, newOffset)

                if(response?.isSuccessful == true){

                    val nuevos = response.body() ?: emptyList()

                    formularios = formularios + nuevos

                    offset = newOffset

                    if(nuevos.size < pageSize){
                        hasMore = false
                    }
                }

            } catch (e: Exception) {

                error = "Error cargando más formularios"

            }
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

            if (response?.isSuccessful == true) {

                response.body()?.contenido

            } else null

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

    //Metodo que permite subir un archivo al servidor (SE TIENE GUARDADO LOCAL)
    suspend fun uploadForm(
        fileUri: Uri,
        fileName: String
    ): Boolean {

        val apiService = api ?: return false

        return uploadUseCase.uploadForm(
            api = apiService,
            fileUri = fileUri,
            fileName = fileName
        )
    }

    //Metodo que permite subir un archivo al servidor (SE TIENE GUARDADO LOCAL)
    suspend fun uploadFormContent(
        content: String,
        fileName: String
    ): Boolean {

        val apiService = api ?: return false

        return uploadUseCase.uploadFormContent(
            api = apiService,
            content = content,
            fileName = fileName
        )
    }

}