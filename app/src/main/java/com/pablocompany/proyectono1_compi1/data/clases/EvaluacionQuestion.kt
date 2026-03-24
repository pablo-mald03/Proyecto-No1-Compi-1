package com.pablocompany.proyectono1_compi1.data.clases

/*Clase que empaqueta a los datos de las preguntas*/
data class EvaluacionQuestion(

    val id: String,
    val titulo: String,
    val respuestaUsuario: Any?,
    val respuestaCorrecta: Any?,
    val esCorrecta: Boolean,
    val esInformativa: Boolean
)