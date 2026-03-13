package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.operadoreslogicos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a la operacion de comparacion logica de OR
public class NodoOr extends Nodo {

    //Atributos
    private Nodo condicionA;
    private Nodo condicionB;

    public NodoOr(Nodo condicionA, Nodo condicionB, int linea, int columna) {
        super(linea, columna);
        this.condicionA = condicionA;
        this.condicionB = condicionB;
    }

    //Metodo que permite retornar la condicion logica que se maneja dentro del OR
    /*
     *
     *  Comparacion tal como se pide en el enunciado (comparacion mayor que 0)
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object valorA = condicionA.ejecutar(tabla, listaErrores);
        double numA = (valorA instanceof Number) ? ((Number) valorA).doubleValue() : 0.0;

        if (numA > 0) {
            return 1.0;
        }

        Object valorB = condicionB.ejecutar(tabla, listaErrores);
        double numB = (valorB instanceof Number) ? ((Number) valorB).doubleValue() : 0.0;

        if (numB > 0) {
            return 1.0;
        }

        return 0.0;
    }

    @Override
    public String getString() {
        return "||";
    }
}
/*Created by Pablo*/