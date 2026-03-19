package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a toda la jerarquia de layouts. Es decir a estos componentes que almacenan dentro de si mismos mas cosas
public abstract class CompiledContenedor extends CompiledForm {

    /*Constructor*/
    public CompiledContenedor(Number width, Number height, Number pointX, Number pointY, List<CompiledEstilos> estilos,int fila, int columna) {
        super(width, height, pointX, pointY,estilos,fila,columna);
    }


    /*Metodo delegado para poder validar los estilos de cada clase contenedor*/
    @Override
    public void ValidarEstilos(List<ErrorAnalisis> listaErrores){

        int contadorBorder = 0;
        int contadorBackgroundColor = 0;
        int contadorColor = 0;
        int contadorTextSize = 0;
        int contadorFontFamily = 0;

        for(CompiledEstilos estilo : this.estilos){

            if(estilo instanceof CompiledBorder){
                contadorBorder++;
            }
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

        validarDuplicado(contadorBorder, "border", listaErrores);
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

}
