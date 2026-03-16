package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase principal de la jerarquia de clases de los formularios
public abstract class Formulario {

    //Atributos
    private int linea;
    private int columna;

    public Formulario(int linea, int columna) {
        this.columna = columna;
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public int getLinea() {
        return linea;
    }

    //Metodo que retorna el codigo compilado del formulario
    public abstract String compilar();

}
