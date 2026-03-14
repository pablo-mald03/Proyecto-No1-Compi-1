package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos;

import java.util.HashMap;

/*Created by Pablo*/
//Clase que permite manejar las tablas de simbolos (Patron experto)
/*
 * Esta tabla de simbolos es una clase que se maneja a si misma para ubicar todas las variables que hay tanto dentro de
 * un bloque de codigo como por fuera (es decir las variables generales del programa)
 * */
public class TablaSimbolos {

    //Atributos
    private HashMap<String, Simbolo> tabla;

    /*Contiene una propia tabla de simbolos que permite manejar las variables dentro*/
    private TablaSimbolos padre;


    //Constructor de tabla de simbolos generales
    public TablaSimbolos() {
        this.tabla = new HashMap<>();
        this.padre = null;
    }

    //Tabla de simbolos para variables dentro de un bloque de codigo
    public TablaSimbolos(TablaSimbolos padre) {
        this.tabla = new HashMap<>();
        this.padre = padre;
    }


    //Metodo que permite insertar a la tabla de simbolos la variable
    //false -> Si el simbolo ya existe
    //true -> Si el simbolo no existe
    public boolean insertar(Simbolo simbolo) {
        if (tabla.containsKey(simbolo.getId())) {
            return false;
        }
        tabla.put(simbolo.getId(), simbolo);
        return true;
    }

    //Metodo que permite buscar el simbolo en la tabla de simbolos por el ID
    public Simbolo buscar(String id) {

        Simbolo simboloEncontrado = tabla.get(id);

        if (simboloEncontrado == null && this.padre != null) {
            return this.padre.buscar(id);
        }

        return simboloEncontrado;

    }


    /*Created by Pablo*/

}
