package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

//Clase que representa a la etiqueta texto dentro del formulario
public class TextoPlano extends Formulario {

    //Atributos caracteristicos del texto
    private Number height;
    private Number width;
    private String texto;
    private EstilosComponent estilos;

    public TextoPlano( Number height, Number width, String texto, EstilosComponent estilos,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.texto = texto;
        this.estilos = estilos;
    }

    @Override
    public String compilar() {
        return "";
    }
}
