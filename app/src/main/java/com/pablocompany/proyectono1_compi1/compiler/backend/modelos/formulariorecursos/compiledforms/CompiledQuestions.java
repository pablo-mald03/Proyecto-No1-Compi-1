package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase de maxima jerarquia que representa a todas las preguntas que puede tener un formulario
public abstract class CompiledQuestions extends CompiledForm {

    public CompiledQuestions(Number width, Number height, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, 0, 0, estilos,fila,columna);
    }


    /*Metodo delegado para poder validar los estilos de cada clase de pregunta*/
    @Override
    public void ValidarEstilos(List<ErrorAnalisis> listaErrores){

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

        validarDuplicado(contadorBackgroundColor, "background color", listaErrores);
        validarDuplicado(contadorFontFamily, "font family", listaErrores);
        validarDuplicado(contadorTextSize, "text size", listaErrores);
        validarDuplicado(contadorColor, "color", listaErrores);
    }

    // Metodo que permite validar la duplicadad de instrucciones en el cuerpo de la seccion
    private void validarDuplicado(int contador, String nombreAtributo, List<ErrorAnalisis> listaErrores) {
        if (contador > 1) {
            listaErrores.add(new ErrorAnalisis("Table", "Semantico",
                    "El atributo \"" + nombreAtributo + "\" ha sido definido mas de una vez en la \"table\".",
                    getFila(), getColumna()));
        }
    }


}/*Created by Pablo*/
