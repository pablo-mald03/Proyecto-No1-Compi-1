package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

import java.util.List;

//Clase que representa a una pregunta drop que puede tener un formulario
public class CompiledDropQuest extends CompiledQuestions {

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

}
/*Created by Pablo*/
