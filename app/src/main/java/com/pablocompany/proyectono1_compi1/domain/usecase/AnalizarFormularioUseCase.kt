package com.pablocompany.proyectono1_compi1.domain.usecase

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.data.clases.ResultadoAnalisis

class AnalizarFormularioUseCase {

    fun ejecutar(codigo: String): ResultadoAnalisis {

        // Aquí después llamarás a tu parser real
        val resultadoParser = ParserWrapper.analizar(codigo)

        return ResultadoAnalisis(
            codigoGenerado = resultadoParser.codigoGenerado,
            errores = resultadoParser.errores
        )
    }
}

//OBJTEDO QUEMADO (FALTA IMPLEMENTACION REAL)
object ParserWrapper {

    fun analizar(codigo: String): ResultadoAnalisis {

        // Simulación temporal CODIGO QUEMADO (FALTA IMPLEMENTACION REAL)
        val errores = mutableListOf<ErrorAnalisis>()

        if (codigo.contains("error")) {
            errores.add(
                ErrorAnalisis(
                    "##",
                    "Sintáctico",
                    "Token inesperado",
                    1,
                    5
                )
            )
        }

        return ResultadoAnalisis(
            codigoGenerado = codigo,
            errores = errores
        )
    }
}