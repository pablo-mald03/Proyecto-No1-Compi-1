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

//Clase que representa a una pregunta multiple que puede tener un formulario
public class CompiledMultipleQuest extends CompiledQuestions {

    //Atributos
    private List<Number> respuesta;
    private List<CompiledCadenaTexto>opciones;

    private CompiledCadenaTexto texto;

    public CompiledMultipleQuest(Number width, Number height, CompiledCadenaTexto texto, List<CompiledCadenaTexto> opciones, List<Number> respuesta, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, estilos, fila, columna);
        this.respuesta = respuesta;
        this.opciones = opciones;
        this.texto = texto;
    }

    /*Metodo que permite validar la semantica del codigo interpretado*/
    public void validarSemantica(List<ErrorAnalisis> listaErrores){

        if(this.height.intValue() < -1 ){
            this.reportarErrores("<multiple>","El atributo \"height\" no debe ser menor a -1", listaErrores);
        }

        if(this.width.intValue() < -1 ){
            this.reportarErrores("<multiple>","El atributo \"width\" no debe ser menor a -1", listaErrores);
        }

        if(!this.respuesta.isEmpty()){
            for(Number num : this.respuesta){
                if(num.intValue() < -1 || num.intValue() >= this.opciones.size()){
                    this.reportarErrores("<multiple>","El valor de la respuesta debe estar dentro de los limites de las opciones o \"vacio\" si no hay respuesta.", listaErrores);
                }
            }
        }

    }

    /*Metodos getter para obtener los atributos de la clase*/
    public List<Number> getRespuesta() {
        return this.respuesta;
    }


    public CompiledCadenaTexto getTexto() {
        return this.texto;
    }

    public List<CompiledCadenaTexto> getOpciones() {
        return this.opciones;
    }

    /*Metodo delegado para poder validar los estilos de cada clase de multiple*/
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

        validarDuplicado(contadorBackgroundColor,"<multiple>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<multiple>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<multiple>", "text size", listaErrores);
        validarDuplicado(contadorColor,"<multiple>", "color", listaErrores);
    }

    /*Metodo que permite que cad clase empaquete sus estilos*/
    @Override
    public  void delegarEstilos(){
        this.empaquetaEstilos();
    }
}
/*Created by Pablo*/