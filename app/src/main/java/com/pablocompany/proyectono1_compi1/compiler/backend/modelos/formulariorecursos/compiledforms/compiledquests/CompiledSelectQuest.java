package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

import java.util.List;

//Clase que representa a una pregunta select que puede tener un formulario
public class CompiledSelectQuest extends CompiledQuestions {

    private Number respuesta;
    private List<String> opciones;

    public CompiledSelectQuest(Number width, Number height, List<String> opciones, Number respuesta, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, estilos, fila, columna);
        this.respuesta = respuesta;
        this.opciones = opciones;
    }

    /*Metodos getter para obtener los atributos de la clase*/
    public Number getRespuesta() {
        return this.respuesta;
    }

    public List<String> getOpciones() {
        return this.opciones;
    }
}
