package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

//Clase que representa a las preguntas abiertas
public class PreguntaAbierta extends Formulario {

    //Atributos caracteristicos de la pregunta abierta
    private Double height;
    private Double width;
    private String label;
    private EstilosComponent estilos;

    public PreguntaAbierta( Double height, Double width, String label, EstilosComponent estilos,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.label = label;
        this.estilos = estilos;
    }


    @Override
    public String compilar() {
        return "";
    }
}
