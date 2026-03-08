package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos;

import java.util.HashMap;

//Clase que permite manejar las tablas de simbolos (Patron experto)
public class TablaSimbolos {

    //Atributos
    private HashMap<String, Simbolo> tabla;

    //Constructor
    public TablaSimbolos() {
        tabla = new HashMap<>();
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
        return tabla.get(id);
    }




}
