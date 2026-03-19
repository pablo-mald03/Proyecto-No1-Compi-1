package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

import java.util.List;

//Clase que representa a una pregunta multiple que puede tener un formulario
public class CompiledMultipleQuest extends CompiledQuestions {

    //Atributos
    private List<Number> respuesta;
    private List<String> opciones;

    public CompiledMultipleQuest(Number width, Number height, List<String> opciones, List<Number> respuesta, CompiledEstilos estilos) {
        super(width, height, estilos);
        this.respuesta = respuesta;
        this.opciones = opciones;
    }

    /*Metodos getter para obtener los atributos de la clase*/
    public List<Number> getRespuesta() {
        return this.respuesta;
    }

    public List<String> getOpciones() {
        return this.opciones;
    }
}
/*Created by Pablo*/