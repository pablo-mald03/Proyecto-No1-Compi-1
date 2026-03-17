package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

//Clase delegada que representa una tabla dentro de un formulario
public class Tablero extends Formulario {

    //Atributos caracteristicos de la table
    private Number height;
    private Number width;

    private Number pointX;
    private Number pointY;

    //Atributo que representa la lista de elementos que tiene la seccion
    private List<List<Formulario>> elementos;
    private EstilosComponent estilos;
    private EstiloBorde borde;

    public Tablero( Number height,  Number width,Number pointX, Number pointY,  List<List<Formulario>> elementos,  EstilosComponent estilos,  EstiloBorde borde ,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.pointX = pointX;
        this.pointY = pointY;
        this.elementos = elementos;
        this.estilos = estilos;
        this.borde = borde;
    }

    //PENDIENTE
    @Override
    public String compilar() {
        return "";
    }
}
