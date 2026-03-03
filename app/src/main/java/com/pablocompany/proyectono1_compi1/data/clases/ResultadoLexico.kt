package com.pablocompany.proyectono1_compi1.data.clases

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.compiler.models.lexerpintado.TokenUI

data class ResultadoLexico(
    val tokens: List<TokenUI>,
    val errores: List<ErrorAnalisis>
)