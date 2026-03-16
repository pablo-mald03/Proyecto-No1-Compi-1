package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
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

    //Metodo que permite validar la semantica del lenguaje generado
    public abstract TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores);

    //Metodo que permite ejecutar los draws en las preguntas (PRIMERA PASADA)
    public void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

    }

    //Metodo que permite ejecutar los requests hacia la pokeAPI en las preguntas (SEGUNDA PASADA)
    public void ejecutarRequests(TablaSimbolos tabla, List<ErrorAnalisis> errores){

    }

}

/*Created by Pablo*/
