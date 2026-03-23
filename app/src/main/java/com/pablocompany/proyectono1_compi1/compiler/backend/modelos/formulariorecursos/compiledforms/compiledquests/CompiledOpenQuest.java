package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a una pregunta abierta que puede tener un formulario
public class CompiledOpenQuest extends CompiledQuestions {

    //Atributo
    private CompiledCadenaTexto texto;

    public CompiledOpenQuest(Number width, Number height, CompiledCadenaTexto texto, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, estilos, fila, columna);
        this.texto = texto;
    }

    /*Metodo que permite validar la semantica del codigo interpretado*/
    public void validarSemantica(List<ErrorAnalisis> listaErrores){

        if(this.height.intValue() < -1 ){
            this.reportarErrores("<open>","El atributo \"height\" no debe ser menor a -1", listaErrores);
        }

        if(this.width.intValue() < -1 ){
            this.reportarErrores("<open>","El atributo \"width\" no debe ser menor a -1", listaErrores);
        }


    }

    /*Metodo getter del texto*/
    public CompiledCadenaTexto getTexto() {
        return this.texto;
    }

    /*Metodo delegado para poder validar los estilos de cada clase de pregunta open*/
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

        validarDuplicado(contadorBackgroundColor,"<open>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<open>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<open>", "text size", listaErrores);
        validarDuplicado(contadorColor,"<open>", "color", listaErrores);
    }

    /*Metodo que permite que cad clase empaquete sus estilos*/
    @Override
    public  void delegarEstilos(){
        this.empaquetaEstilos();
    }

}/*Created by Pablo*/
