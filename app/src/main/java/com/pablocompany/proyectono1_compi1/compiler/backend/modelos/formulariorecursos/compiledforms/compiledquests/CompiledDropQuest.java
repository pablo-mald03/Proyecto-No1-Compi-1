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

//Clase que representa a una pregunta drop que puede tener un formulario
public class CompiledDropQuest extends CompiledQuestions {

    //Atributos
    private Number respuesta;
    private List<CompiledCadenaTexto> opciones;

    private CompiledCadenaTexto texto;

    public CompiledDropQuest(Number width, Number height, CompiledCadenaTexto texto, List<CompiledCadenaTexto> opciones, Number respuestas, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, estilos, fila, columna);
        this.respuesta = respuestas;
        this.opciones = opciones;
        this.texto = texto;
    }

    /*Metodos getter para obtener los atributos de la clase*/
    public Number getRespuesta() {
        return this.respuesta;
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

        validarDuplicado(contadorBackgroundColor,"<drop>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<drop>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<drop>", "text size", listaErrores);
        validarDuplicado(contadorColor,"<drop>", "color", listaErrores);
    }
}
/*Created by Pablo*/
