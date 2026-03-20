package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a la tabla que es contenedora de codigo
public class CompiledTable extends CompiledContenedor {

    //Atributo que representa al contenido de la tabla
    private List<List<CompiledForm>> filas;

    public CompiledTable(Number width, Number height, Number pointX, Number pointY, List<List<CompiledForm>> filas, List<CompiledEstilos> estilos,int fila, int columna) {
        super(width, height, pointX, pointY, estilos,fila,columna);
        this.filas = filas;
    }

    /*Metodos getters y setters que permiten obtener la lista de componentes*/
    private List<List<CompiledForm>> getElementos() {
        return this.filas;
    }

    /*Metodo que permite calcular el total de columas que tendra el layout*/
    public int getTotalColumnas(){

        int max = 0;
        for (List<CompiledForm> fila : filas) {
            if (fila.size() > max) {
                max = fila.size();
            }
        }

        return max;

    }

    /*Metodo delegado para poder validar los estilos para table*/
    @Override
    public void validarEstilos(List<ErrorAnalisis> listaErrores){

        if (this.estilos == null) {
            return;
        }

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

        validarDuplicado(contadorBorder,"<table>", "border", listaErrores);
        validarDuplicado(contadorBackgroundColor,"<table>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<table>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<table>", "text size", listaErrores);
        validarDuplicado(contadorColor, "<table>","color", listaErrores);


        for(List<CompiledForm> fila : this.filas){
            if (fila != null) {
                for (CompiledForm form : fila) {
                    if (form != null) {
                        form.validarEstilos(listaErrores);
                    }
                }
            }
        }
    }
}

/*Created by Pablo*/
