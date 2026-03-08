package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Jerarquia principal de clases que se implementa A CUALQUIER INSTRUCCION/EXPRESION que se encuentre dentro del codigo fuente
public abstract class Nodo {

    //Atributos
    private int linea;
    private int columna;

    public Nodo(int linea, int columna) {
        this.columna = columna;
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public int getLinea() {
        return linea;
    }

    //Metodos generales que se implementan en las demas jerarquias
    public abstract Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores);

    public abstract String getString();

}

/*Created by Pablo*/
