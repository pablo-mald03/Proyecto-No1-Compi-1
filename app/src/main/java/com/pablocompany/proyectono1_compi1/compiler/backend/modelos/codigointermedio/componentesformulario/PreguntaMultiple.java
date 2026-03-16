package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

/*Clase delegada que representa a una pregunta multiple*/
public class PreguntaMultiple extends Formulario {

    //Atributos caracteristicos de la pregunta multiple
    private Number height;
    private Number width;

    //Atributo que representa la lista de opciones que tiene la pregunta multiple
    private List<String> opciones;

    private EstilosComponent estilos;

    private List<Integer> respuestasCorrectas;

    public PreguntaMultiple(Number height, Number width, List<String> opciones, List<Integer> respuestas, EstilosComponent estilos, int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.opciones = opciones;
        this.estilos = estilos;
        this.respuestasCorrectas = respuestas;
    }

    //PENDIENTE
    @Override
    public String compilar() {
        return "";
    }
}
