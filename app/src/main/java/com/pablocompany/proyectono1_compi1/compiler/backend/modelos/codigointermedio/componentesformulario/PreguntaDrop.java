package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

//Clase que representa al objeto de la pregunta drop
public class PreguntaDrop extends Formulario {

    //Atributos caracteristicos de la pregunta drop
    private Number height;
    private Number width;

    //Atributo que representa la lista de opciones que tiene la pregunta drop
    private List<String> opciones;

    private EstilosComponent estilos;

    private Integer respuestaCorrecta;

    public PreguntaDrop( Number height, Number width, List<String> opciones, Integer respuestaCorrecta, EstilosComponent estilos,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.opciones = opciones;
        this.estilos = estilos;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    //PENDIENTE
    @Override
    public String compilar() {
        return "";
    }
}
