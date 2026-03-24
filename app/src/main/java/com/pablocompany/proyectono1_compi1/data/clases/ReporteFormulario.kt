package com.pablocompany.proyectono1_compi1.data.clases

/*Clase utilizada para capturar a todas las preguntas que permiten validar las respuestas*/
data class ReporteFormulario(
    val detalles: Map<String, EvaluacionQuestion>,
    val aciertos: Int,
    val total: Int,
    val porcentaje: Float
)