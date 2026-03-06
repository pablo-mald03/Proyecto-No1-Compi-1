package com.pablocompany.proyectono1_compi1.domain.usecase

import com.pablocompany.proyectono1_compi1.compiler.logic.LexerForms
import com.pablocompany.proyectono1_compi1.compiler.logic.sym
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis
import com.pablocompany.proyectono1_compi1.compiler.models.lexerpintado.TokenUI
import com.pablocompany.proyectono1_compi1.data.clases.ResultadoLexico
import java.io.StringReader


class AnalizarLexicoUseCase {

    operator fun invoke(codigo: String): ResultadoLexico {

        val lexer = LexerForms(StringReader(codigo))

        val listaTokens = mutableListOf<TokenUI>()
        val listaErrores = mutableListOf<ErrorAnalisis>()

        while (true) {

            val token = lexer.next_token() ?: break

            if (token.sym == sym.EOF) break

            val lexema = token.value?.toString() ?: ""

            listaTokens.add(
                TokenUI(
                    lexema,
                    token.sym,
                    token.left,
                    token.right
                )
            )

            if (token.sym == sym.ERROR) {
                listaErrores.add(
                    ErrorAnalisis(
                        lexema,
                        "Lexico",
                        "Simbolo no existe en el lenguaje",
                        token.left,
                        token.right
                    )
                )
            }
        }

        return ResultadoLexico(listaTokens, listaErrores)
    }
}