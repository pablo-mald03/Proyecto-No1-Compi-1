package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa un color definido por el usuario. Es decir que represena a la JERARQUIA PRINCIPAL DE COLORES
public abstract class NodoColor {

    //Atributos
    private int linea;
    private int columna;

    public NodoColor(int linea, int columna) {
        this.columna = columna;
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public int getLinea() {
        return linea;
    }

    //Metodo que permite retornar los valores del color en formato String
    public abstract String getString();

    //Metodo principal que retorna el color en formato RGB (util en frontend)
    public abstract int [] evaluarColor(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores);

    public abstract Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores);

    //Metodo que permite validar la semantica dentro de un color
    public abstract  TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout);

    /*--Metodo propio de la clase que permite contar los comodines que tienen en el color--*/
    public abstract int contarComodines();

    //Metodo que permite clonar el color
    public abstract NodoColor clonar();

}

/*Created by P*/

