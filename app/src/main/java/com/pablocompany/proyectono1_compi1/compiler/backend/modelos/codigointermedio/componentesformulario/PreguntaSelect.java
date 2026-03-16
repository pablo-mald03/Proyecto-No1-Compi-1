package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

//Clase que representa a las preguntas de tipo select para los formularios
public class PreguntaSelect extends Formulario {

    //Atributos caracteristicos de la pregunta select
    private Number height;
    private Number width;
    private String label;

    //Atributo que representa la lista de opciones que tiene la pregunta select
    private List<String> opciones;

    private EstilosComponent estilos;

    public PreguntaSelect( Double height, Double width, String label, List<String> opciones, EstilosComponent estilos,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.label = label;
        this.opciones = opciones;
        this.estilos = estilos;
    }

    //PENDIENTE
    @Override
    public String compilar() {
        return "";
    }
}
