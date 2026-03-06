package com.pablocompany.proyectono1_compi1.data.clases

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis

data class ResultadoAnalisis(
    val codigoGenerado: String,
    val errores: List<ErrorAnalisis>
)