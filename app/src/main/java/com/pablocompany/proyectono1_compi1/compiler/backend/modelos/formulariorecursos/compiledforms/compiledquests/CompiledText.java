package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a un texto plano
public class CompiledText extends CompiledQuestions {

    private CompiledCadenaTexto texto;

    public CompiledText(Number width, Number height, CompiledCadenaTexto texto, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, estilos, fila, columna);
        this.texto = texto;
    }

    /*Metodo getter del texto*/
    public CompiledCadenaTexto getTexto() {
        return this.texto;
    }

    /*Metodo delegado para poder validar los estilos de cada clase de text*/
    @Override
    public void validarEstilos(List<ErrorAnalisis> listaErrores){

        if (this.estilos == null) {
            return;
        }

        int contadorBackgroundColor = 0;
        int contadorColor = 0;
        int contadorTextSize = 0;
        int contadorFontFamily = 0;

        for(CompiledEstilos estilo : this.estilos){

            if(estilo instanceof CompiledBackground) {
                contadorBackgroundColor++;
            }

            if(estilo instanceof CompiledTextColor) {
                contadorColor++;
            }

            if(estilo instanceof CompiledTextSize){
                contadorTextSize++;
            }

            if(estilo instanceof CompiledFontFamily){
                contadorFontFamily++;
            }

        }

        validarDuplicado(contadorBackgroundColor,"<text>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<text>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<text>", "text size", listaErrores);
        validarDuplicado(contadorColor,"<text>", "color", listaErrores);
    }

}/*Created by Pablo*/
