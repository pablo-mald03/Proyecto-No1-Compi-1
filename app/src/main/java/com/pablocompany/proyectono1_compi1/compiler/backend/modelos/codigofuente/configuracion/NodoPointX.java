package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que define la configuracion de pointX de configuracion de la section
public class NodoPointX extends Nodo {

    //Atributos
    private NodoExpresion expresion;

    public NodoPointX(NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return expresion.ejecutar(tabla,listaErrores);
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "pointY: " + this.expresion.getString();
    }
}

